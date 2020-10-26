/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.helio.valkyrie.valkyr.ie.connector;

import helio.framework.exceptions.NotReachableEndpointException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pablo
 */
public class Main {
    
    
    public static void main(String [] args) throws NotReachableEndpointException{
     List<String> argss= new ArrayList();
     
     String text="Airbus is slowly restarting its assembly line in China. General Motors began limited production on Saturday. Toyota followed on Monday morning.\n" +
"\n" +
"Fitfully and painfully — and with some worried prodding from Beijing — China is trying to reopen for business.\n" +
"\n" +
"The world’s second-largest economy practically shut down three weeks ago as a viral outbreak sickened tens of thousands of people, unexpectedly lengthening a Chinese holiday. The freeze set off warnings that the global economy could be in jeopardy if the world’s pre-eminent manufacturing powerhouse stayed shut for long.\n" +
"\n" +
"Now, as some factories rumble back into action, the monumental task of restarting China is becoming clear. China’s efforts to contain the virus are clashing with its push to get the country back to work, requiring the country’s leaders to strike a balance between keeping people safe and getting vital industries back on track.\n" +
"\n" +
"Chinese leaders called this past week for more emphasis on reviving the economy. But many of the factories that have reopened are operating well below capacity, say companies and experts. Quarantines, blocked roads and checkpoints are stopping millions of workers from returning to their jobs. Supply lines have been severed.\n" +
"\n" +
"Even to start up again, Chinese officials are requiring businesses to provide masks to workers, record their temperatures and track their movements to make sure they haven’t come into contact with the coronavirus, named COVID-19.\n" +
"\n" +
"This is your last free article.\n" +
"Subscribe to the Times\n" +
"“The kind of fear and freeze that has taken hold in terms of economic activity is likely to persist,” said George Magnus, a research associate at Oxford University’s China Center. “I don’t really see a good outcome.”\n" +
"\n" +
"By Monday, more than 70,000 people had been infected by the coronavirus and over 1,700 had died worldwide, according to officials. New infections continue to be confirmed around the world, including an American who was identified with the disease in Malaysia on Sunday who had been on a cruise ship, raising concerns about another potential cluster outside mainland China.\n" +
"\n" +
"Also on Sunday, Taiwan said that a 61-year-old man who had a history of poor health but not known for travel to China had died of the coronavirus, making him the fifth fatality outside the mainland.\n" +
"\n" +
"Editors’ Picks\n" +
"\n" +
"Never Mind the Internet. Here’s What’s Killing Malls.\n" +
"\n" +
"Is Coffee Good for You?\n" +
"\n" +
"‘It’s Pretty Brutal’: The Sandwich Generation Pays a Price\n" +
"Still, the pace of new cases officially confirmed in mainland China, the center of the outbreak, has slowed over the past three days.\n" +
"\n" +
"The ripples have continued to spread around the world. Prime Minister Lee Hsien Loong of Singapore warned on Friday that the city-state could fall into recession as a result of the outbreak. Germany, Europe’s business powerhouse, on Friday reported slowing economic growth at the end of 2019, prompting fears the virus could delay a recovery.";
     
   argss.add("C:/Users/Pablo/Documents/GitHub/valkyr-ie/resources");
   argss.add(text);
   //https://www.nytimes.com/2020/02/17/business/china-coronavirus-economy.html
    
    ValkyrConnector valcon= new ValkyrConnector(argss);
    String s= valcon.retrieveData();
    
        System.out.println(s);
    }
    
   
    
    
}
