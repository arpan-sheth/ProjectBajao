import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL; 
import java.util.List;

import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Segment;
import com.echonest.api.v4.TimedEvent;
import com.echonest.api.v4.Track;
import com.echonest.api.v4.TrackAnalysis;

public class testTrack {
	static EchoNestAPI en;
	static boolean trace = true;
	static final String API_KEY="CBM3KQXFGVFDUMD9S";
	static BufferedWriter outFile ;
	static String outFileName = "/Users/arpan/Desktop/echoNestApi";

	public static void setUpClass() throws EchoNestException {

		en = new EchoNestAPI(API_KEY);
		en.setMinCommandTime(0);
		en.setTraceSends(trace);
		en.setTraceRecvs(trace);
		try {
			outFile = new BufferedWriter(new FileWriter(outFileName, true));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void verifyAnalysis(TrackAnalysis analysis, boolean full) {
		try {
			outFile.newLine();
			outFile.write("num samples : " + analysis.getNumSamples());
			outFile.write("sample md5  : " + analysis.getMD5());
			outFile.write("num channels: " + analysis.getNumChannels());
			outFile.write("duration    : " + analysis.getDuration());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (full) {
			try {
				outFile.newLine();
				outFile.write("sections");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			List<TimedEvent> sections = analysis.getSections();
			for (TimedEvent e : sections) {
				try {
					outFile.newLine();

					outFile.write(e+"");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println(indentJson(e.toString()));
			}

			try {
				outFile.newLine();
				outFile.write("bars");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			List<TimedEvent> bars = analysis.getBars();
			for (TimedEvent e : bars) {
				try {
					outFile.newLine();
					outFile.write(e+"");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println(indentJson(e.toString()));
			}

			try {
				outFile.newLine();
				outFile.write("beats");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			List<TimedEvent> beats = analysis.getBeats();
			for (TimedEvent e : beats) {
				try {
					outFile.newLine();
					outFile.write(e+"");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println(indentJson(e.toString()));
			}

			try {
				outFile.newLine();
				outFile.write("tatums");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			List<TimedEvent> tatums = analysis.getTatums();
			for (TimedEvent e : tatums) {
				try {
					outFile.newLine();

					outFile.write(e+"");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println(indentJson(e.toString()));
			}

			try {
				outFile.newLine();

				outFile.write("segments");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			List<Segment> segments = analysis.getSegments();
			for (Segment e : segments) {
				try {
					outFile.newLine();

					outFile.write(e+"");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println(indentJson(e.toString()));
			}
		}
	}

	private static void verifyTrack(Track track) throws EchoNestException {
		track.showAll();
		System.out.println(track.getAnalysisURL() != null?"has analysis url " + track.getAnalysisURL() :false );
		verifyAnalysis(track.getAnalysis(), true);
	}


	public static void main(String args[]) throws EchoNestException, IOException, URISyntaxException, InterruptedException{
		setUpClass();
		//trackUploadFromURLTest();
		String mp3Array[] = setMP3Array();
		String str = "";
		//for (int i=0;i<mp3Array.length;i++){
		for (int i=0;i<2;i++){
			System.out.println(mp3Array[i]);
			str = mp3Array[i];
			str= str.replaceAll(" ","%20");
			//	String str="http://bajega.com/songs_all/songs12."+i+".mp3";
			outFileName = "/Users/arpan/Desktop/echoNestApi/"+i+".dat";
			try {
				URL url = new URL(str);
				Track track = en.uploadTrack(url, false);
				track.waitForAnalysis(30000);
				verifyTrack(track);	
				Thread.sleep(35000);				
			}catch (Exception ex){
				System.out.println("["+i+"]"+ex);
			}

		}


	}

	public  static String indentJson(String stringToIndent){
		char [] c = stringToIndent.toCharArray();
		int count = 0;
		StringBuffer sb= new StringBuffer();
		for (char ct:c){
			if (ct=='{'||ct==','||ct=='}'){
				if (ct=='{'){
					sb.append(ct);
					sb.append("\n\t");
					for (int i=0; i<count;i++){
						sb.append("\t");
					}
					count++;
				}
				if (ct==','){
					sb.append(ct);
					sb.append("\n");
					for (int i=0; i<count;i++){
						sb.append("\t");
					}
				}
				if (ct=='}'){
					count--;
					sb.append("\n");
					for (int i=0; i<count+1;i++){
						sb.append("\t");
					}
					sb.append(ct);
				}
			}else 
				sb.append(ct);
		}
		return sb.toString();
	}

	public static String [] setMP3Array(){
		String [] mp3Array = { 
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Dabangg (2010)/10 - Dabangg Theme - Salman Khan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Do Dilon Ke Khel Mein (2010)/01 - Jaane Kyun Pyaar Mein - Daboo Malik & Shweta Pandit @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Do Dilon Ke Khel Mein (2010)/02 - Resham Ka Dupatta - Rekha Bhardwaj @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Do Dilon Ke Khel Mein (2010)/03 - Ajnabee Ehsas Ko - Soham Chakraborty @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Do Dilon Ke Khel Mein (2010)/04 - Samajhdaar Ko Ishara - Sunidhi Chauhan, Sandeep Acharya, Harshdeep, Daboo Malik & A D Boyz @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Do Dilon Ke Khel Mein (2010)/05 - Rang Dalunga Chunri (Holi) - Udit Narayan, Shreya Ghoshal, Dipak Giri & Shalini Srivastava @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Do Dilon Ke Khel Mein (2010)/06 - Teri Yaad Aayi - Kailash Kher @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Do Dilon Ke Khel Mein (2010)/07 - Jaane Kyun Pyaar Mein (Remix) - Daboo Malik & Pavni Pandey @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Do Dooni Chaar (2010)/01 - Baaja Bajeya - Sunidhi Chauhan & Meet Brothers Anjan Ankit @ fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Do Dooni Chaar (2010)/02 - Do Dooni Chaar - Shankar Mahadevan & Vishal Dadlani @ fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Do Dooni Chaar (2010)/03 - Ek Haath De - Meet Brothers Anjan Ankit @ fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Do Dooni Chaar (2010)/04 - Maange Ki Ghodi - Rakesh Pandit & Krishna @ fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Do Dooni Chaar (2010)/05 - Do Dooni Chaar (Jam) - Shankar Mahadevan & Vishal Dadlani @ fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Do Dooni Chaar (2010)/06 - Do Dooni Chaar (Club Mix) - Shankar Mahadevan, Vishal Dadlani & Meet Brothers @ fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Do Dooni Chaar (2010)/07 - Ek Haath De (Dj Phukan Mix) - Meet Brothers Anjan Ankit  @ fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Dulha Mil Gaya (2010)/01 - Dulha Mil Gaya - Daler Mehndi @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Dulha Mil Gaya (2010)/02 - Akela Dil - Adnan Sami & Anushka Manchanda @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Dulha Mil Gaya (2010)/03 - Aaja Aaja Mere Ranjhna - Swananda & Anushka Manchanda @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Dulha Mil Gaya (2010)/04 - Magar Meri Jaan - Anushka Manchanda, Mahua & Lalit Pandit @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Dulha Mil Gaya (2010)/05 - Tu Jo Jaan Le - Sonu Nigam @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Dulha Mil Gaya (2010)/06 - Rang Diya Dil - Shreya Ghoshal @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Dulha Mil Gaya (2010)/07 - Dilrubaon Ke Jalwe - Amit Kumar & Monali Thakur @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Dulha Mil Gaya (2010)/08 - Rang Diya Dil (Sad Version) - Shreya Ghoshal @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Dulha Mil Gaya (2010)/09 - Shiri Farhad - Neeraj Shridhar & Tulsi Kumar @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Dulha Mil Gaya (2010)/10 - Akela Dil (Remix) - Adnan Sami & Anushka Manchanda @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Dulha Mil Gaya (2010)/11 - Dulha Mil Gaya (Remix) - Daler Mehndi @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Dulha Mil Gaya (2010)/12 - Dilrubaon Ke Jalwe (Remix) - Amit Kumar & Monali Thakur @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Dus Tola (2010)/01 - Aisa Hota Tha - Mohit Chauhan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Dus Tola (2010)/02 - Lal Lal Aag Hua - Sukhwinder Singh @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Dus Tola (2010)/03 - Tumse Kya Kehna - Sonu Nigam @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Dus Tola (2010)/04 - Jee Na Jalaiyo (Female) - Sunidhi Chauhan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Dus Tola (2010)/05 - Jee Na Jalaiyo (Male) - Sukhwinder Singh @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/For Real (2010)/01 - How Funny (Concert) - Lynn Ray Pardo @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/For Real (2010)/02 - Plastic Doll - Samantha Edwards @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/For Real (2010)/03 - Puppeteer (India) - Samantha Edwards @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/For Real (2010)/04 - How Funny (Montage) - Lynn Ray Pardo @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/For Real (2010)/05 - No Blame - Lynn Ray Pardo @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/For Real (2010)/06 - Puppeteer (Live) - Samantha Edwards @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/For Real (2010)/07 - Thats How Funny (Instrumental) - Instrumental @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/For Real (2010)/08 - Puppeteer (Instrumental) - Instrumental @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/For Real (2010)/09 - Plastic Doll (Instrumental) - Instrumental @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/For Real (2010)/10 - Puppeteer (USA Instrumental) - Instrumental @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Golmaal 3 (2010)/01 - Golmaal - K.K., Anouska Manchanda & Monali Thakur @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Golmaal 3 (2010)/02 - Apna Har Din - Shaan & Anouska Manchanda @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Golmaal 3 (2010)/03 - Ale - Neeraj Sridhar & Antara Mitra @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Golmaal 3 (2010)/04 - Desi Kali - Neeraj Sridhar & Sunidhi Chauhan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Golmaal 3 (2010)/05 - Golmaal (Remix) - K.K., Anouska Manchanda & Monali Thakur @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Golmaal 3 (2010)/06 - Apna Har Din (Remix) - Shaan & Anouska Manchanda @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Golmaal 3 (2010)/07 - Desi Kali (Remix) - Neeraj Sridhar & Sunidhi Chauhan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Golmaal 3 (2010)/08 - Disco Dancer - Bappi Lahiri @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Golmaal 3 (2010)/09 - Yaad Aa Raha Hain - Sudesh Bhosle @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Gumshuda (2010)/01 - Tanha Rahien - Sunidhi Chauhan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Gumshuda (2010)/02 - Dhundo - Sonu Nigam @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Gumshuda (2010)/03 - Kisne Pehchana - June Banerjee @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Gumshuda (2010)/04 - Chup Tha Paani - Ronita De @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Gumshuda (2010)/05 - Is Mein Hai Chamak - Rupankar @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Gumshuda (2010)/06 - Khasi Tingya - Dohar @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Gumshuda (2010)/07 - Dhundo (Remix) - Sonu Nigam @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Guzaarish (2010)/01 - Guzaarish - K. K. & Shail Hada @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Guzaarish (2010)/02 - Sau Gram Zindagi - Kunal Ganjawala @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Guzaarish (2010)/03 - Tera Zikr - Shail Hada & Rakesh Pandit @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Guzaarish (2010)/04 - Saiba - Vibhavari Joshi, Francois Castellino & Shail Hada @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Guzaarish (2010)/05 - Jaane Kiske Khwaab - K. K. @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Guzaarish (2010)/06 - Udi - Sunidhi Chauhan & Shail Hada @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Guzaarish (2010)/07 - Keh Na Sakoon - Shail Hada @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Guzaarish (2010)/08 - Chaand Ki Katori - Harshdeep Kaur @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Guzaarish (2010)/09 - Daayein Baayein - K. K. @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Guzaarish (2010)/10 - Dhundli Dhundli - Shankar Mahadevan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hello Darling (2010)/01 - Aa Jaane Jaan - Jaaved Jaffrey, Akriti Kakar & Antara Mitra @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hello Darling (2010)/02 - Band Baaja - Richa Sharma, Ritu Pathak & Rana Mazumder @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hello Darling (2010)/03 - Dil To Saala - Sunidhi Chauhan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hello Darling (2010)/04 - Attrah Baras Ki - Suzanne DMello @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hello Darling (2010)/05 - Working Girls - Shweta Pandit, Ritu Pathak & Priyadarshini @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hello Darling (2010)/06 - Band Baaja (Remix) - Richa Sharma, Ritu Pathak & Rana Mazumder @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Help (2010)/01 - Kehna Hai - Suzanne DMello & Joi Barua @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Help (2010)/02 - Kyun Gum Hai Khushiyan - Ashu & Kirti Sagathia @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Help (2010)/03 - Help - Rana Mazumder & Barkha @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Help (2010)/04 - Incubated - Just For Death - Ashu @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Help (2010)/05 - Kehna Hai (Remix) - Suzanne DMello & Joi Barua @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Help (2010)/06 - Kyun Gum Hai Khushiyan (Remix) - Ashu & Kirti Sagathia @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Help (2010)/07 - Help (Remix) - Rana Mazumder & Barkha @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hide & Seek (2010)/01 - Jingle Bells - Krishna @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hide & Seek (2010)/02 - Kaise Jiyu - Krishna @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hide & Seek (2010)/03 - Maula - Suraj Jagan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hide & Seek (2010)/04 - Hide & Seek - Suraj Jagan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hide & Seek (2010)/05 - Friday Night - Chirantan Bhatt, Aanchal & Jaspreet Kohli @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hide & Seek (2010)/06 - Jingle Bells (Bell Mix) - Krishna @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hide & Seek (2010)/07 - Kaise Jiyu (Mix) - Krishna @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hide & Seek (2010)/08 - Friday Night (Mix) - Chirantan Bhatt, Aanchal & Jaspreet Kohli @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hisss (2010)/01 - Lagi Lagi Milan Dhun Lagi - Shreya Ghoshal @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hisss (2010)/02 - Beyond The Snake - Shruti Hassan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hisss (2010)/03 - Sway (Mann Dole) - Mallika Sherawat @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hisss (2010)/04 - I Got That Poison (Hisss Remix) - Shweta Pandit @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hisss (2010)/05 - Lagi Lagi Milan Dhun Lagi (Version 2) - Sunidhi Chauhan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hisss (2010)/06 - Hisss - Shweta Pandit @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hisss (2010)/07 - Lafanaa - Sunidhi Chauhan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hisss (2010)/08 - Naina Miley (Robot) - A. R. Rahman, Lady Kash N Krissy & Suzanne @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hisss (2010)/09 - Rishte Naate (De Dana Dan) - Rahat Fateh Ali Khan & Suzanne @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hisss (2010)/10 - Kilimanjaro (Robot) - Javed Ali & Chinmayi @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Housefull (2010)/01 - Oh Girl Youre Mine - Tarun Sagar, Alyssa Mendonsa & Loy Mendonsa @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Housefull (2010)/02 - Papa Jag Jayega - Ritu Pathak, Neeraj Shridhar & Alyssa Mendonsa @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Housefull (2010)/03 - Aapka Kya Hoga (Dhanno) - Mika, Sunidhi Chauhan & Sajid Khan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Housefull (2010)/04 - I Dont Know What To Do - Shabbir Kumar & Sunidhi Chauhan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Housefull (2010)/05 - Loser - Vivienne Pocha & Amitabh Bhattacharya @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Housefull (2010)/06 - Oh Girl Youre Mine (O Boy What A Girli Mix) - Tarun Sagar, Alyssa Mendonsa & Loy Mendonsa @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Housefull (2010)/07 - Papa Jag Jayega (Insane Insomaniac Mix) - Ritu Pathak, Neeraj Shridhar & Alyssa Mendonsa @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Housefull (2010)/08 - I Dont Know What To Do (Shabbirs Sexy 70S Mix) - Shabbir Kumar & Sunidhi Chauhan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hum Tum Aur Ghost (2010)/01 - Dekho Raste Mein - KK & Shreya Ghoshal @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hum Tum Aur Ghost (2010)/02 - Hum Tum Aur Ghost - Shankar Mahadevan, Vishal Dadlani, Mani Mahadevan, Arun Ingle, Jaya Hardikar & Jyotsna Hardikar @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hum Tum Aur Ghost (2010)/03 - Banware Se Pooche Banwariya - Shaan, Sunidhi Chauhan, Maria Goretti, Loy Mendonsa, Raman Mahadevan, Anusha Mani, Rahul Saxena & Kaushik Deshpande @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hum Tum Aur Ghost (2010)/04 - Kal Tum The Yahan - Caralisa Monteiro, Shankar Mahadevan, Dominique Cerejo & Clinton Cerejo @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hum Tum Aur Ghost (2010)/05 - Hum Tum Aur Ghost (Remix) - Shankar Mahadevan, Vishal Dadlani & Bob Bobcat Omulo @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hum Tum Aur Ghost (2010)/06 - Dekho Raste Mein (Remix) - KK, Shreya Ghoshal & Arshad Warsi @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Hum Tum Aur Ghost (2010)/07 - Kal Tum The Yahan (Remix) - Caralisa Montiero & Shankar Mahadevan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/I Hate Luv Storys (2010)/01 - Jab Mila Tu - Vishal Dadlani @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/I Hate Luv Storys (2010)/02 - Bin Tere - Shafqat Amanat Ali & Sunidhi Chauhan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/I Hate Luv Storys (2010)/03 - I Hate Luv Storys - Vishal Dadlani @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/I Hate Luv Storys (2010)/04 - Bahara - Shreya Ghoshal, Sona Mohapatra @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/I Hate Luv Storys (2010)/05 - Sadka - Suraj Jagan & Mahalaxmi Iyer @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/I Hate Luv Storys (2010)/06 - Bin Tere (Reprise) - Shekhar Ravjiani @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/I Hate Luv Storys (2010)/07 - Bahara (Chill Version) - Rahat Fateh Ali Khan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/I Hate Luv Storys (2010)/08 - Bin Tere (Remix) - Shafqat Amanat Ali & Sunidhi Chauhan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Ishqiya (2010)/01 - Dil To Bachcha Hai - Rahat Fateh Ali Khan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Ishqiya (2010)/02 - Ibn-E-Batuta - Sukhwinder Singh & Mika @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Ishqiya (2010)/03 - Ab Mujhe Koi - Rekha Bhardwaj @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Ishqiya (2010)/04 - Badi Dheere Jali - Rekha Bhardwaj @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Ishqiya (2010)/05 - Dil To Bachcha Hai (Remix) - Rahat Fateh Ali Khan & Clinton Cerejo @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Ishqiya (2010)/06 - Ibn-E-Batuta (Nucleya Remix) - Sukhwinder Singh & Mika @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Ishqiya (2010)/07 - Ibn-E-Batuta (Remix) - Sukhwinder Singh & Mika @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jaane Kahan Se Aayi Hai (2010)/01 - Keh Do Zara - Rashid Ali @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jaane Kahan Se Aayi Hai (2010)/02 - Nacha Main - Sonu Nigam & Somya Rao @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jaane Kahan Se Aayi Hai (2010)/03 - Jaane Kahan Se Aayi Hai - Shaan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jaane Kahan Se Aayi Hai (2010)/04 - Koi Rok Bhi Lo - Sonu Nigam & Sunidhi Chauhan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jaane Kahan Se Aayi Hai (2010)/05 - Jaane Kahan Se Aayi Hai (Remix) - Shaan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jaane Kahan Se Aayi Hai (2010)/06 - Keh Do Zara (Remix) - Rashid Ali @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jhootha Hi Sahi (2010) (2 CDs)/CD1 - Soundtrack/01 - Cry Cry - Rashid Ali & Shreya Ghoshal @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jhootha Hi Sahi (2010) (2 CDs)/CD1 - Soundtrack/02 - Maiyya Yashoda (Jamuna Mix) - Javed Ali & Chinmayi @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jhootha Hi Sahi (2010) (2 CDs)/CD1 - Soundtrack/03 - Hello Hello - Karthik @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jhootha Hi Sahi (2010) (2 CDs)/CD1 - Soundtrack/04 - Do Nishaaniyan - Sonu Nigam @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jhootha Hi Sahi (2010) (2 CDs)/CD1 - Soundtrack/05 - Pam Pa Ra - Shreya Ghoshal @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jhootha Hi Sahi (2010) (2 CDs)/CD1 - Soundtrack/06 - Ive Been Waiting - Vijay Yesudas @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jhootha Hi Sahi (2010) (2 CDs)/CD1 - Soundtrack/07 - Maiyya Yashoda (Thames Mix) - Javed Ali & Chinmayi @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jhootha Hi Sahi (2010) (2 CDs)/CD1 - Soundtrack/08 - Do Nishaaniyan (Heartbreak Reprise) - Sonu Nigam @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jhootha Hi Sahi (2010) (2 CDs)/CD1 - Soundtrack/09 - Call Me Dil - Rashid Ai @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jhootha Hi Sahi (2010) (2 CDs)/CD2 - Karaoke/01 - Cry Cry - Instrumental @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jhootha Hi Sahi (2010) (2 CDs)/CD2 - Karaoke/02 - Maiyya Yashoda (Jamuna Mix) - Instrumental @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jhootha Hi Sahi (2010) (2 CDs)/CD2 - Karaoke/03 - Hello Hello - Instrumental @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jhootha Hi Sahi (2010) (2 CDs)/CD2 - Karaoke/04 - Do Nishaaniyan - Instrumental @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jhootha Hi Sahi (2010) (2 CDs)/CD2 - Karaoke/05 - Pam Pa Ra - Instrumental @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jhootha Hi Sahi (2010) (2 CDs)/CD2 - Karaoke/06 - Ive Been Waiting - Instrumental @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jhootha Hi Sahi (2010) (2 CDs)/CD2 - Karaoke/07 - Maiyaa Yashoda (Thames Mix) - Instrumental @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jhootha Hi Sahi (2010) (2 CDs)/CD2 - Karaoke/08 - Do Nishaaniyan (Heartbreak Reprise) - Instrumental @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Jhootha Hi Sahi (2010) (2 CDs)/CD2 - Karaoke/09 - Call Me Dil - Instrumental @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Kajraare (2010)/01 - Kajra Kajra Kajraare - Himmesh Reshammiya & Sunidhi Chauhan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Kajraare (2010)/02 - Rabba Luck Barsa - Himmesh Reshammiya @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Kajraare (2010)/03 - Aafreen - Himmesh Reshammiya & Harshdeep Kaur @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Kajraare (2010)/04 - Tujhe Dekh Ke Armaan Jaage - Himmesh Reshammiya  & Shreya Ghoshal @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Kajraare (2010)/05 - Teriyan Meriyan - Himmesh Reshammiya & Shreya Ghoshal @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Kajraare (2010)/06 - Woh Lamha Phir Se Jeena Hai - Himmesh Reshammiya & Harshdeep Kaur @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Kajraare (2010)/07 - Sanu Guzara Zamana - Himmesh Reshammiya & Sunidhi Chauhan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Kajraare (2010)/08 - Kajra Kajra Kajraare (Party Mix) - Himmesh Reshammiya & Sunidhi Chauhan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Kajraare (2010)/09 - Rabba Luck Barsa (Party Mix) - Himmesh Reshammiya @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Kajraare (2010)/10 - Woh Lamha Phir Se Jeena Hai (Party Mix) - Himmesh Reshammiya & Harshdeep Kaur @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Kajraare (2010)/11 - Sanu Guzara Zamana (Lounge Mix) - Himmesh Reshammiya & Sunidhi Chauhan @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Karthik Calling Karthik (2010)/01 - Hey Ya - Clinton, Shankar Mahadevan & Loy @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Karthik Calling Karthik (2010)/02 - Uff Teri Adaa - Shankar Mahadevan & Alyssa Mendonsa @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Karthik Calling Karthik (2010)/03 - Jaane Ye Kya Hua - K.K. @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Karthik Calling Karthik (2010)/04 - Kaisi Hai Ye Udaasi - Kailash Kher & Sukanya Purayastha @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Karthik Calling Karthik (2010)/05 - Karthik Calling Karthik - Suraj Jaggan, Shankar Mahadevan, Caralisa, Malika @ Fmw11.com.mp3",
				"http://download.apunkabollywood.com/songs/Audio/indian/movies/Karthik Calling Karthik (2010)/06 - Karthik 2.0 - Midival Punditz & Karsh Kale @ Fmw11.com.mp3",
		}; 
		return mp3Array;
	}
}