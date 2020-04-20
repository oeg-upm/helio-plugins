package helio.connector.hyperledger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallet.Identity;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;

import helio.framework.Connector;
import helio.framework.exceptions.NotReachableEndpointException;

public class HyperledgerConnector implements Connector {

	static {
		System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "false");
	}
	
	private String resultToHelio;
	
	/**
	 * Arg0 -> IP:PORT
	 * Arg1 -> AdminUser
	 * Arg2 -> AdminPass
	 * Arg3 -> mspID
	 * Arg4 -> NewUser
	 * Arg5 -> Affiliation
	 * Arg6 -> Channel
	 * Arg7 -> Contract
	 * Arg8 -> Query
	 */
	public HyperledgerConnector(List<String> arguments) {
		try {
			//Params: ipPort + Admin + passAdmin + mspId
			UserLogin(arguments.get(0),arguments.get(1),arguments.get(2),arguments.get(3));
			//Params: ipPort + Admin + newUser + mspID + Affiliation
			createUser(arguments.get(0),arguments.get(1),arguments.get(4),arguments.get(3),arguments.get(5));
			//Params: newUser + Admin + mspID + Affiliation + Channel + Contract + Query
			resultToHelio = retrieveResults(arguments.get(4),arguments.get(1),arguments.get(3),arguments.get(5),arguments.get(6),arguments.get(7),arguments.get(8));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Send data to HELIO
	 */
	@Override
	public String retrieveData() throws NotReachableEndpointException {
		return resultToHelio;
	}
	
	/**
	 * Login -WITH ADMIN-. This method allow us create an external user, in order to query the chain
	 * @param ipPort
	 * @param username
	 * @param password
	 * @param mspId
	 * @throws Exception
	 */
	public void UserLogin(String ipPort, String username, String password, String mspId) throws Exception {
		Properties props = new Properties();
		//Load the public certificate
		props.put("pemFile","certificates/ca.org1.example.com-cert.pem");
		HFCAClient caClient = HFCAClient.createNewInstance(ipPort, props);
		CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
		caClient.setCryptoSuite(cryptoSuite);
		// Create a wallet for managing identities
		Wallet wallet = Wallet.createFileSystemWallet(Paths.get("wallet"));
		// Check to see if we've already enrolled the admin user.
		boolean adminExists = wallet.exists(username);
        if (adminExists) {
            System.out.println(username + " already logged");
            return;
        }
        // Enroll the admin user with member service.
        final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
        // Hostname associate with the certificate
        enrollmentRequestTLS.addHost("localhost");
        // Name of the signing profile to use when issuing the certifiacate
        enrollmentRequestTLS.setProfile("tls");
        Enrollment enrollment = caClient.enroll(username, password, enrollmentRequestTLS);
        Identity user = Identity.createIdentity(mspId, enrollment.getCert(), enrollment.getKey());
        wallet.put(username, user);
		System.out.println(username + " login sucessfully");
	}
	
	/**
	 * Create the external user and generate his certificates. Admin account must be logged.
	 * @param ipPort
	 * @param adminUser
	 * @param newUser
	 * @param mspId
	 * @param affiliation
	 * @throws Exception
	 */
	public void createUser(String ipPort, String adminUser, String newUser, String mspId, String affiliation) throws Exception {
		// Create a CA client for interacting with the CA.
		Properties props = new Properties();
		props.put("pemFile","certificates/ca.org1.example.com-cert.pem");
		HFCAClient caClient = HFCAClient.createNewInstance(ipPort, props);
		CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
		caClient.setCryptoSuite(cryptoSuite);
		// Create a wallet for managing identities
		Wallet wallet = Wallet.createFileSystemWallet(Paths.get("wallet"));
		// Check to see if we've already enrolled the user.
		boolean userExists = wallet.exists(newUser);
		if (userExists) {
			System.out.println("The user " + newUser + " already exists");
			return;
		}
		userExists = wallet.exists(adminUser);
		if (!userExists) {
			System.out.println("One admin account must be enrolled in order to create the new user");
			return;
		}
		Identity adminIdentity = wallet.get(adminUser);
		HLUser admin = new HLUser();
		admin.setName(adminUser);
		admin.setAffiliation(affiliation);
		admin.setRoles(null);
		admin.setAccount(null);
		Enrollment enroll = new Enrollment() {
			@Override
			public PrivateKey getKey() {
				return adminIdentity.getPrivateKey();
			}
			
			@Override
			public String getCert() {
				return adminIdentity.getCertificate();
			}
		};
		admin.setEnrollment(enroll);
		admin.setMspId(mspId);
		// Register the user, enroll the user, and import the new identity into the wallet.
		RegistrationRequest registrationRequest = new RegistrationRequest(newUser);
		registrationRequest.setAffiliation(affiliation);
		registrationRequest.setEnrollmentID(newUser);
		String enrollmentSecret = caClient.register(registrationRequest, admin);
		Enrollment enrollment = caClient.enroll(newUser, enrollmentSecret);
		Identity user = Identity.createIdentity(mspId, enrollment.getCert(), enrollment.getKey());
		wallet.put(newUser, user);
		System.out.println("Successfully created " + newUser + ". Certs files are located in wallet folder");
	}
	
	/**
	 * Perform the query and return the result in JSON
	 * @param newUser
	 * @param adminUser
	 * @param mspId
	 * @param affiliation
	 * @param channel
	 * @param contractR
	 * @param query
	 * @return
	 * @throws Exception
	 */
    public String retrieveResults(String newUser, String adminUser, String mspId, String affiliation, String channel, String contractR, String query) throws Exception {
    	byte[] result;
		//Wallet folder to manage identities
		Path walletPath = Paths.get("wallet");
		Wallet wallet = Wallet.createFileSystemWallet(walletPath);
		//Configuration file of the chain
		Path networkConfigPath = Paths.get("connectionFiles/connection-org1.yaml");
		Gateway.Builder builder = Gateway.createBuilder();
		builder.identity(wallet, newUser).networkConfig(networkConfigPath).discovery(true);
		//Create gateway
		try (Gateway gateway = builder.connect()) {
			Network network = gateway.getNetwork(channel);
			//Get contract name (fabcar)
			Contract contract = network.getContract(contractR);
			//Results
			result = contract.evaluateTransaction(query);
			Identity adminIdentity = wallet.get(adminUser);
			HLUser admin = new HLUser(adminUser,affiliation,mspId,null,adminIdentity);
		}
		String toHelio = new String(result); 
		return toHelio;
	}

    //EXAMPLE
    public static void main(String [] args) {
		List<String> arguments = Arrays.asList("https://192.168.6.30:7054","admin","adminpw","Org1MSP","externalUser","org1.department1","mychannel","fabcar","queryAllCars");
		HyperledgerConnector hc = new HyperledgerConnector(arguments);
    }
    
}
