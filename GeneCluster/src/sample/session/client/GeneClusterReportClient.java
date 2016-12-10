package sample.session.client;

import java.io.*;
import david.xsd.*;
import org.apache.axis2.AxisFault;
import sample.session.client.*;
import sample.session.service.xsd.*;
import sample.session.client.stub.DAVIDWebServiceStub;

public class GeneClusterReportClient{
    public void invokeService(){
    try {
        DAVIDWebServiceStub stub =
                new DAVIDWebServiceStub(
                "https://david.abcc.ncifcrf.gov/webservice/services/DAVIDWebService.DAVIDWebServiceHttpSoap12Endpoint/");
            stub._getServiceClient().getOptions().setManageSession(true);

        System.out.println();
        //user authentication by email address
 		//For user registration, go to http://david.abcc.ncifcrf.gov/webservice/register.htm
	    String userVerification= (String) stub.authenticate("1532745146@qq.com");

	    System.out.println("User authentication: " +  userVerification);
	    System.out.println();
	   if (userVerification.equals("true"))
	   {
		//addlist
	    String inputIds = new String("1112_g_at,1331_s_at,1355_g_at,1372_at,1391_s_at,1403_s_at,1419_g_at,1575_at,1645_at,1786_at,1855_at,1890_at,1901_s_at,1910_s_at,1937_at,1974_s_at,1983_at,2090_i_at,31506_s_at,31512_at,31525_s_at,31576_at,31621_s_at,31687_f_at,31715_at,31793_at,31987_at,32010_at,32073_at,32084_at,32148_at,32163_f_at,32250_at,32279_at,32407_f_at,32413_at,32418_at,32439_at,32469_at,32680_at,32717_at,33027_at,33077_at,33080_s_at,33246_at,33284_at,33293_at,33371_s_at,33516_at,33530_at,33684_at,33685_at,33922_at,33963_at,33979_at,34012_at,34233_i_at,34249_at,34436_at,34453_at,34467_g_at,34529_at,34539_at,34546_at,34577_at,34606_s_at,34618_at,34623_at,34629_at,34636_at,34702_f_at,34703_f_at,34720_at,34902_at,34972_s_at,35038_at,35069_at,35090_g_at,35091_at,35121_at,35169_at,35213_at,35367_at,35373_at,35439_at,35566_f_at,35595_at,35648_at,35896_at,35903_at,35915_at,35956_s_at,35996_at,36234_at,36317_at,36328_at,36378_at,36421_at,36436_at,36479_at,36696_at,36703_at,36713_at,36766_at,37061_at,37096_at,37097_at,37105_at,37166_at,37172_at,37408_at,37454_at,37711_at,37814_g_at,37898_r_at,37905_r_at,37953_s_at,37954_at,37968_at,37983_at,38103_at,38128_at,38201_at,38229_at,38236_at,38482_at,38508_s_at,38604_at,38646_s_at,38674_at,38691_s_at,38816_at,38926_at,38945_at,38948_at,39094_at,39187_at,39198_s_at,39469_s_at,39511_at,39698_at,39908_at,40058_s_at,40089_at,40186_at,40271_at,40294_at,40317_at,40350_at,40553_at,40735_at,40790_at,40959_at,41113_at,41280_r_at,41489_at,41703_r_at,606_at,679_at,822_s_at,919_at,936_s_at,966_at,967_g_at");
		String idType = new String("AFFYMETRIX_3PRIME_IVT_ID");
	    String listName = new String("make_up");
	    int listType = 0;
	    double addListOutput =0;

	    //Set user defined categories
		//String category_names = new String("BBID,BIOCARTA,COG_ONTOLOGY,INTERPRO,KEGG_PATHWAY,OMIM_DISEASE,PIR_SUPERFAMILY,SMART,SP_PIR_KEYWORDS,UP_SEQ_FEATURE");
		//to use DAVID dafault categories, set category_names to be empty
		String category_names = new String("");
		String validCategoryString = stub.setCategories(category_names);

	    StringBuffer sb;
	    String comma = ", ";
	    sb=new StringBuffer();
	    try{
			addListOutput = (double) stub.addList(inputIds, idType, listName, listType);
			String allListName_str = (String)stub.getAllListNames();
			String[] allListNames = allListName_str.split(",");
		System.out.println("Test getGeneClusterReport():");
		int overlap = 3;
		int initialSeed = 2;
		int finalSeed = 2;
		double linkage = 0.5;
		int kappa = 35;
		System.out.println("\tSet overlap=" + overlap + " initialSeed=" +initialSeed+ " finalSeed="+ finalSeed + " linkage=" +linkage + " kappa="+kappa);
		System.out.println();
		SimpleGeneClusterRecord[] simpleGeneClusterRecords=(SimpleGeneClusterRecord[])stub.getGeneClusterReport(overlap,initialSeed,finalSeed,linkage,kappa);
		if (simpleGeneClusterRecords.length >0)
		{
		    for (int k = 0; k<simpleGeneClusterRecords.length; k++)
		    {
		        sb.append(simpleGeneClusterRecords[k].getName()+"\tEnrichment Score: " + simpleGeneClusterRecords[k].getScore());
			writeListRecords(simpleGeneClusterRecords[k].getListRecords(),sb);
		    }
		}
		PrintWriter outfile = new PrintWriter(new FileOutputStream("geneClusterReport.txt"));
		outfile.println(sb);
		outfile.close();
		System.out.println("\ngeneClusterReport.txt generated.\n");
		}catch (java.io.IOException e) {e.printStackTrace();
	}catch (Exception e) {System.out.println("Unknown exception:"+e.toString());}}
        }catch (AxisFault axisFault) { axisFault.printStackTrace();
        }catch (java.rmi.RemoteException e) {e.printStackTrace();}
    }

    protected void writeListRecords(ListRecord[] recordList, StringBuffer sb) {
	sb.append("\nID\tGene Name\n");
	for (int j = 0; j < recordList.length; j++)
	{
	    String[] values = (String[])recordList[j].getValues();
	    sb.append(values[0]+"\t");
	    sb.append(recordList[j].getName()+"\n");
	}
	sb.append("\n");
    }

    public static void main(String[] args) throws java.io.IOException {
        new GeneClusterReportClient().invokeService();
    }
}