package helio.connector.hyperledger;

import java.security.PrivateKey;
import java.util.Set;

import org.hyperledger.fabric.gateway.Wallet.Identity;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

public class HLUser implements User{

	private String name;
	private Set<String> roles;
	private String account;
	private String affiliation;
	private Enrollment enrollment;
	private String mspId;
	private Identity adminIdentity;
	
	public HLUser() {
		
	}
	
	public HLUser(String name, String affiliation, String mspId, Enrollment enrollment, Identity adminIdentity) {
        this.name = name;
        this.affiliation = affiliation;
        this.enrollment = enrollment;
        this.mspId = mspId;
        this.adminIdentity = adminIdentity;
	}
	
	public Identity getAdminIdentity() {
		return adminIdentity;
	}
	
	public void setAdminIdentity(Identity adminIdentity) {
		this.adminIdentity = adminIdentity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public String getMspId() {
		return mspId;
	}

	public void setMspId(String mspId) {
		this.mspId = mspId;
	}
	
    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

	public Enrollment getEnrollment() {
		return enrollment;
	}
	
}
