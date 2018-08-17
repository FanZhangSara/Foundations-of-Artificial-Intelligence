import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.*;


class prediacte{
    String symbol;
    String name;
    String[] var;
}

class sentence{
    List<prediacte> list = new ArrayList<prediacte>();
}

class homework1{
    List<String> strq= new ArrayList<>();
    List<String> strkb= new ArrayList<>();
    List<prediacte> query = new ArrayList<>();
    List<sentence> list = new ArrayList<>();

    public void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;

            int numq = Integer.parseInt(reader.readLine());  //num of query
            //strq = new ArrayList<>();
            for (int i = 0; i < numq && (tempString = reader.readLine()) != null;i++) {
                strq.add(tempString);
            }

            int numkb = Integer.parseInt(reader.readLine());  //num of kb
            // strkb = new ArrayList<>();
            while ((tempString = reader.readLine()) != null) {
                strkb.add(tempString);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }


    public void output(String[] ret) throws Exception{
        File file = new File("output.txt");
        FileWriter fw = new FileWriter(file);
        PrintWriter bw = new PrintWriter(fw);
        for(String v : ret){
            bw.write(v);
            bw.println();
        }
        bw.close();
        fw.close();
    }


    public List<sentence> kb(){
        //List<sentence> list = new ArrayList<>();
        int num = 0;
        for(int i = 0; i < strkb.size();i++){
            sentence sen = new sentence();

            String[] s = strkb.get(i).split("\\|");
            for(int m = 0; m < s.length;m++){
//                if(s[m].charAt(0)==' ')
//                    System.out.println("yes");
                //s[m].trim();
                //System.out.println(":"+s[m]);
                for(int j = 0; j< s[m].length();j++){
                    if(s[m].charAt(j)!=' '){
                        s[m]=s[m].substring(j);
                        break;
                    }
                }
            }
            for(int j = 0; j < s.length;j++){

                prediacte pre = new prediacte();
                String[] names  = s[j].split("\\(" );
                names[0].trim();
                //names[0] = name;
                String[] var1 = names[1].split("\\)");
                String vars = var1[0];
                String[] var = vars.split(",");
                if(names[0].charAt(0)=='~'){
                    pre.symbol = names[0].substring(0,1);
                    pre.name = names[0].substring(1,names[0].length());
                }else{
                    pre.symbol="+";
                    pre.name = names[0];
                }

                for(int m = 0; m < var.length;m++){
                    if(var[m].charAt(0)>='a' && var[m].charAt(0)<='z'){
                        var[m] = var[m]+i;
                    }
                }
                pre.var= var;
                sen.list.add(pre);
            }
            list.add(sen);
        }
        return list;
    }

    public List<prediacte> qury(){
        //List<prediacte> query = new ArrayList<>();
        for(int i = 0; i < strq.size();i++){
            prediacte pre = new prediacte();
            String[] names  = strq.get(i).split("\\(" );
            String vars = names[1].substring(0,names[1].length()-1);
            String[] var = vars.split(",");
            if(names[0].charAt(0)=='~'){
                pre.symbol = "~";
                pre.name = names[0].substring(1,names[0].length());
            }else{
                pre.symbol="+";
                pre.name = names[0];
            }
            for(int m = 0; m < var.length;m++){
                if(var[m].charAt(0)>='a' && var[m].charAt(0)<='z'){
                    int n = list.size()+i;
                    var[m] = var[m]+n;
                }
            }
            pre.var = var;
            query.add(pre);
        }
        return query;
    }

    public boolean[] resolution(){
        boolean[] res = new boolean[query.size()];
        for(int i = 0; i < query.size(); i++){

            long startTime = System.currentTimeMillis();
            prediacte precur = query.get(i);
            if(precur.symbol=="+")
                precur.symbol = "~";
            else
                precur.symbol = "+";
            // precur.symbol, precur.name, precur.val []
            //add query
            List<sentence> newlist = new ArrayList<>();
            //newlist = list;
            for(int num = 0; num < list.size();num++){
                sentence s1 = new sentence();
                for(int d = 0; d < list.get(num).list.size();d++){
                    prediacte pre = new prediacte();
                    pre.symbol = list.get(num).list.get(d).symbol;
                    pre.name = list.get(num).list.get(d).name;
                    pre.var = new String[list.get(num).list.get(d).var.length];
                    for(int t = 0; t < list.get(num).list.get(d).var.length;t++){
                        pre.var[t] = list.get(num).list.get(d).var[t];
                    }
                    s1.list.add(pre);
                }
                newlist.add(s1);
//                                for(int d = 0; d < list.get(m).list.size();d++){
//                                    prediacte pre = new prediacte();
//                                    pre.symbol = newlist.get(m).list.get(d).symbol;
//                                    pre.name = newlist.get(m).list.get(d).name;
//                                    pre.var = new String[newlist.get(m).list.get(d).var.length];
//                                    for(int t = 0; t < newlist.get(m).list.get(d).var.length;t++){
//                                        pre.var[t] = newlist.get(m).list.get(d).var[t];
//                                    }
//                                    s1.list.add(pre);
//                                }
            }


            prediacte p = new prediacte();
            p.symbol = precur.symbol;

            p.var = precur.var;
            p.name = precur.name;
            sentence s = new sentence();
            s.list.add(p);
            newlist.add(s);
            int location = newlist.size() - 1;

            while(location < newlist.size()) {
                if(res[i]==true)
                    break;
                for (int m = 0; m < location; m++) {
                    if(res[i]==true)
                        break;
                    int len = newlist.get(m).list.size();
                    for (int n = 0; n < len; n++) {
                        if(res[i]==true)
                            break;
                        for (int n1 = 0; n1 < newlist.get(location).list.size(); n1++) {
                            if(res[i]==true)
                                break;
                            prediacte pren = newlist.get(m).list.get(n);
                            prediacte preloc = newlist.get(location).list.get(n1);
                            if ((pren.name.equals(preloc.name) ) && (!pren.symbol.equals(preloc.symbol)) && (pren.var.length == preloc.var.length) &&notunify(pren,preloc)) {

                                //System.out.println("unifys");
//                                sentence s1 = new sentence();
//                                for(int d = 0; d < newlist.get(m).list.size();d++){
//                                    prediacte pre = new prediacte();
//                                    pre.symbol = newlist.get(m).list.get(d).symbol;
//                                    pre.name = newlist.get(m).list.get(d).name;
//                                    pre.var = new String[newlist.get(m).list.get(d).var.length];
//                                    for(int t = 0; t < newlist.get(m).list.get(d).var.length;t++){
//                                        pre.var[t] = newlist.get(m).list.get(d).var[t];
//                                    }
//                                    s1.list.add(pre);
//                                }
//                                sentence s2 = new sentence();
//                                for(int d = 0;d<newlist.get(location).list.size();d++){
//                                    prediacte pre = new prediacte();
//                                    pre.symbol = newlist.get(location).list.get(d).symbol;
//                                    pre.name = newlist.get(location).list.get(d).name;
//                                    pre.var = new String[newlist.get(location).list.get(d).var.length];
//                                    for(int t = 0; t < newlist.get(location).list.get(d).var.length;t++){
//                                        pre.var[t] = newlist.get(location).list.get(d).var[t];
//                                    }
//                                    s2.list.add(pre);
//                                }


                                boolean bool = false;
                                HashMap<String,String> map = unify(pren, preloc, newlist, m, n, location, n1,bool);
                                if(bool)
                                    continue;
                                System.out.println("unify");
                                sentence newsen = new sentence();


                                for (int z = 0; z < len; z++) {
                                    if (!pren.equals(newlist.get(m).list.get(z)) ) {
                                        prediacte newpre = new prediacte();
                                        newpre.name = newlist.get(m).list.get(z).name;
                                        newpre.symbol = newlist.get(m).list.get(z).symbol;
                                        String[] var = new String[newlist.get(m).list.get(z).var.length];
                                        for(int num = 0; num < newlist.get(m).list.get(z).var.length;num++){
                                            String str = newlist.get(m).list.get(z).var[num];
                                            if(map.containsKey(str)){
                                                var[num] = map.get(str);
                                            }
                                            else{
                                                var[num] = str;
                                            }
                                        }
                                        newpre.var = var;
                                        newsen.list.add(newpre);
                                    }
                                }
                                for (int z = 0; z < newlist.get(location).list.size(); z++) {
                                    if (!preloc.equals(newlist.get(location).list.get(z)) ) {
                                        prediacte newpre = new prediacte();
                                        newpre.name = newlist.get(location).list.get(z).name;
                                        newpre.symbol = newlist.get(location).list.get(z).symbol;
                                        String[] var = new String[newlist.get(location).list.get(z).var.length];
                                        for(int num = 0; num < newlist.get(location).list.get(z).var.length;num++){
                                            String str = newlist.get(location).list.get(z).var[num];
                                            if(map.containsKey(str)){
                                                var[num] = map.get(str);
                                            }
                                            else{
                                                var[num] = str;
                                            }
                                        }
                                        newpre.var = var;
                                        newsen.list.add(newpre);
                                    }
                                }


                                for(int te = 0; te < newsen.list.size(); te++){
                                    System.out.print(location+" "+"symbol"+newsen.list.get(te).symbol+"  function name"+newsen.list.get(te).name +"  ");
                                    for(int i1 = 0; i1 < newsen.list.get(te).var.length;i1++){
                                        System.out.print(newsen.list.get(te).var[i1]);
                                    }
                                    System.out.println();
                                }
                                System.out.println();
                                if (newsen.list.size()==0){
                                    res[i] = true;
                                    break;
                                }

                                boolean adjust = false;

                                for(int num = 0; num <newlist.size();num++){
                                    if(newsen.list.size()==newlist.get(num).list.size()){
                                        for(int num1 = 0; num1 < newsen.list.size();num1++){
                                            if(newsen.list.get(num1).name.equals(newlist.get(num).list.get(num1).name)&&newsen.list.get(num1).symbol.equals(newlist.get(num).list.get(num1).symbol)&&same(newsen.list.get(num1).var,newlist.get(num).list.get(num1).var)){
                                                adjust = true;
                                            }
                                        }
                                    }
                                }
                                if(adjust)
                                    continue;
                                newlist.add(newsen);
                            }
                        }
                    }
                }
                //if (location < newlist.size()) {
                    location++;
         //      if(location>40)
           //         break;
                //long startTime = System.currentTimeMillis();
                long endTime = System.currentTimeMillis();
                long diff = endTime - startTime;
                if(diff > 50000){
                    break;
                }
               // }
            }
            //for(int j = 0;j < )
//            for(int m= 0; m < newlist.size();m++){
//                for(int n = m+1; n < newlist.size();n++){
//                    //newlist.get(m);
//                    //newlist.get(n);
//                    for(int x = 0; x < newlist.get(m).list.size();x++){
//                        for(int y = 0; y < newlist.get(n).list.size();y++){
//                            prediacte prex = newlist.get(m).list.get(x);
//                            prediacte prey = newlist.get(n).list.get(y);
//                            if((prex.name==prey.name)&&(prex.symbol!=prey.symbol)&&(prex.var.length==prey.var.length)){
//                                //unify
//                                unify(prex,prey);
//
//                            }
//                        }
//                    }
//                }
//            }

            //res[i] = false;
        }

        return res;
    }

    public boolean notunify(prediacte pren,prediacte preloc){
        for(int i = 0; i < pren.var.length;i++){
            if(preloc.var[i].charAt(0)>='A' && preloc.var[i].charAt(0)<='Z'&&pren.var[i].charAt(0)>='A' && pren.var[i].charAt(0)<='Z'){
                if(preloc.var[i].equals(pren.var[i]))
                    continue;
                else
                    return false;
        }
        }
        return true;
    }


    //newlist = unify(pren, preloc, s1, s2, location, n1); pren, preloc, newlist, m, n, location, n1)
    public HashMap<String, String> unify(prediacte pren, prediacte preloc,List newlist, int m, int n, int location, int n1,boolean bool){
        HashMap<String,String> map = new HashMap<>();
        for(int i = 0; i<pren.var.length;i++){
            //s.charAt(i)>='A'&&s.charAt(i)<='Z
            if(pren.var[i].charAt(0)>='A' && pren.var[i].charAt(0)<='Z'){
                String valr = preloc.var[i];
                String valR = pren.var[i];
                if(map.containsKey(valr)) {
                    if(!map.get(valr).equals(valR)){
                        bool = true;
                        return map;
                    }

                }
                map.put(valr,valR);
//                for(int p = 0; p < s2.list.size();p++ ){
//                    for(int q = 0; q < s2.list.get(p).var.length;q++){
//                        if(vals.equals(s2.list.get(p).var[q]))
//                            s2.list.get(p).var[q]=prex.var[i];
//                    }
//                }
            }

            else if(preloc.var[i].charAt(0)>='A' && preloc.var[i].charAt(0)<='Z'){
                String valr = pren.var[i];
                String valR = preloc.var[i];
                if(map.containsKey(valr)) {
                    //map.get(valr).equals(valR);
                    if(!map.get(valr).equals(valR)){
                        bool = true;
                        return map;
                    }
                }
                map.put(valr,valR);
            }

            else{
                String valr = pren.var[i];
                String valR = preloc.var[i];
                map.put(valr,valR);
            }
        }
        return map;
    }

    public boolean same(String[] var1, String[] var2){
        for(int i = 0; i < var1.length;i++){
            if(!var1[i].equals(var2[i]))
                return false;
        }
        return true;
    }


}

public class hw {


    public static void main(String[] args) throws Exception{
        homework1 h=new homework1();
        h.readFileByLines("input.txt");

        List<sentence> arr = h.kb();
//        for(int i = 0; i < arr.size();i++){
//            sentence sen = arr.get(i);
//            for(int j = 0; j < sen.list.size();j++){
//                System.out.print(sen.list.get(j).var[0]);
//            }
//            System.out.println();
//        }
        //readFileByLines;
        //h.strq;
        //h.strkb;
        List<prediacte> arr1 = h.qury();
//        for(int i = 0; i < arr1.size();i++){
//            System.out.println(arr1.get(i).var[0]);
//        }

        //System.out.println(h.kb());

        boolean[] res = h.resolution();
        String[] ret = new String[res.length];

        for(int i =0;i<res.length;i++){
            if(res[i])
                ret[i] ="TRUE";
            else
                ret[i] = "FALSE";
        }
        for(int i =0;i<res.length;i++){
            System.out.println(ret[i]);
        }
        h.output(ret);

    }

}

