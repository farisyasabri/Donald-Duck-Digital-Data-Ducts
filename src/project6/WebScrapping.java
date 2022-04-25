package project6;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class WebScrapping {
    public void mainWebScraper(){
        Scanner s=new Scanner(System.in);
        System.out.print("Enter a file name to store data : ");
        String file=s.nextLine()+".csv";
        System.out.println("1 : 250 top rated movie");
        System.out.println("2 : Box Office Movies");
        System.out.println("3 : Countries in Europe");
        System.out.print("Choose : ");
        int ch=s.nextInt();
        switch(ch){
            case 1 -> topMovie(file);
            case 2 -> boxOffice(file);
            case 3 -> countryCity(file);
        }
        Manipulation ask = new Manipulation();
        ask.askQuestion();
    }

    public void topMovie(String file){
        String url="https://www.imdb.com/chart/top/?ref_=nv_mv_250";
        try{
            PrintWriter pw=new PrintWriter(new FileOutputStream(file));
            Document doc=Jsoup.connect(url).get();
            Elements e=doc.select("tbody.lister-list");
            System.out.println(e.select("tr").size());
            pw.println("Title,Year,Rating");
            for(Element r:e.select("tr")){
                String[] title=r.select("td.posterColumn img").attr("alt").split(",");
                for(String  f:title)
                    pw.print(f+" ");
                pw.print(",");
                String year=r.select("td.titleColumn span.secondaryInfo").text();
                pw.print(year.replaceAll("[^\\d]", "")+",");    //exclude ()
                String rating=r.select("td.ratingColumn.imdbRating ").text();
                pw.println(rating);
            }
            pw.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void boxOffice(String file){

        String url="https://www.imdb.com/chart/boxoffice?pf_rd_m=A2FGELUUNOQJNL&pf_rd_p=4da9d9a5-d299-43f2-9c53-f0efa18182cd&pf_rd_r=1FM6MV9DCV1RB6SMC05E&pf_rd_s=right-4&pf_rd_t=15506&pf_rd_i=top&ref_=chttp_ql_1";
        try{
            PrintWriter pw=new PrintWriter(new FileOutputStream(file));
            Document doc=Jsoup.connect(url).get();
            Elements e=doc.select("tbody");
            System.out.println(e.select("tr").size());
            pw.println("Title,Weekend,Gross,Weeks");
            for(Element r:e.select("tr")){
                String title=r.select("td.posterColumn img").attr("alt");
                pw.print(title+",");
                String[] weekend=r.select("td.ratingColumn").text().split(" ");
                pw.print(weekend[0]+",");
                String gross=r.select("td.ratingColumn span.secondaryInfo").text();
                pw.print(gross+",");
                String weeks=r.select("td.weeksColumn").text();
                pw.println(weeks);
            }
            pw.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void countryCity(String file){
        String url="https://www.worlddata.info/capital-cities.php";
        try{
            PrintWriter pw=new PrintWriter(new FileOutputStream(file));
            Document doc=Jsoup.connect(url).get();
            Elements e=doc.select("table#tabsort1.std100.hover tbody");
            System.out.println(e.select("tr").size());
            pw.println("Country,Capital City,Population");
            for(Element r:e.select("tr")){
                String country=r.select("td a.fl_eu").text();
                pw.print(country+",");
                String[] minus=country.split(" ");
                String city[]=r.select("td").text().split(" ");
                for(int i=minus.length;i<city.length-1;i++){
                    pw.print(city[i]+" ");
                }
                pw.print(",");
                String[] pp=city[city.length-1].split(",");
                for(String a:pp)
                    pw.print(a+" ");
                pw.println();
            }
            pw.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
