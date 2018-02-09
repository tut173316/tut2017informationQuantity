package s4.b173316; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID.
import java.lang.*;
import s4.specification.*;

/*
 interface FrequencerInterface {     // This interface provides the design for frequency counter.
 void setTarget(byte[]  target); // set the data to search.
 void setSpace(byte[]  space);  // set the data to be searched target from.
 int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
 //Otherwise, it return 0, when SPACE is not set or Space's length is zero
 //Otherwise, get the frequency of TAGET in SPACE int subByteFrequency(int start, int end);
 // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
 // For the incorrect value of START or END, the behavior is undefined.
 */

public class Frequencer implements FrequencerInterface{
    // Code to start with: This code is not working, but good start point to work.
    byte [] myTarget;
    byte [] mySpace;
    boolean targetReady = false;
    boolean spaceReady = false;
    int [] suffixArray;
    int [] a;
    
    private void printSuffixArray() {
        if(spaceReady) {
            for(int i=0; i< mySpace.length; i++) {
                //suffixArrayから開始位置を取得
                int s = suffixArray[i];
                
                //開始位置から最後までの文字列を出力
                for(int j=s;j < mySpace.length;j++) {
                    System.out.write(mySpace[j]);
                }
                System.out.write('\n');
            }
        }
    }
    
    private int suffixCompare(int i, int j) {
        int si = suffixArray[i];
        int sj = suffixArray[j];
        int s = 0;
        if(si > s) s = si;
        if(sj > s) s = sj;
        int n = mySpace.length - s;
        for(int k = 0; k < n; k++){
            if(mySpace[si+k] > mySpace[sj + k]) return 1;
            if(mySpace[si+k] < mySpace[sj + k]) return -1;
        }
        if(si < sj) return 1;
        if(si > sj) return -1;
 
        return 0;
    }
    
    /*
     * マージ
     * 2つの配列a1[]とa2[]を併合してa[]を作ります。
     */
    
    void merge(int[] a1,int[] a2,int[] a){
        int i=0,j=0;
        while(i<a1.length || j<a2.length){
            if(j>=a2.length || (i<a1.length && (suffixCompare(a1[i],a2[j])==-1))){
                a[i+j]=a1[i];
                i++;
            }
            else{
                a[i+j]=a2[j];
                j++;
            }
            /*
            for(int s = 0; s < a.length; s++){
                System.out.print(a[s] + " ");
            }
            System.out.println();
             */
        }
    }
    
    /*
     * マージソート
     * 既にソート済みの2つの配列を併合して新しい配列を
     * 生成することで、データのソートを行います。
     */
    
    void mergeSort(int[] a){
        if(a.length>1){
            int m=a.length/2;
            int n=a.length-m;
            int[] a1=new int[m];
            int[] a2=new int[n];
            for(int i=0;i<m;i++){
                 a1[i]=a[i];
            }
            for(int i=0;i<n;i++){
                a2[i]=a[m+i];
            }
            mergeSort(a1);
            mergeSort(a2);
            merge(a1,a2,a);
        }
    }

    public void setSpace(byte []space) {
        mySpace = space;
        if(mySpace.length>0) spaceReady = true;
        int temp = 0;
        suffixArray = new int[space.length];
        a = new int[space.length];
        // put all suffixes in suffixArray. Each suffix is expressed by one interger.
        for(int i = 0; i< space.length; i++) {
            suffixArray[i] = i;
            a[i] = i;
        }
        //ソート部分
        mergeSort(a);
        suffixArray = a;
        
        /*
         //バブルソート
        for(int i = 0; i < mySpace.length-1; i++){
            for(int k = i+1; k < mySpace.length; k++){
                if(suffixCompare(i,k)==1){
                    temp = suffixArray[i];
                    suffixArray[i] = suffixArray[k];
                    suffixArray[k] = temp;
                }
            }
        }*/
        
        for(int i = 0; i < mySpace.length; i++){
            //System.out.println(suffixArray[i]);
            byte[] bytetmp = new byte[15];
            int b = 0;
            for(int k = suffixArray[i]; k < mySpace.length; k++){
                //String str = new String();
                //System.out.println(k+" : "+mySpace[k]);
                bytetmp[b] = mySpace[k];
                b++;
            }
            System.out.println(i + ":" + suffixArray[i] + ", " +new String(bytetmp));
        }

        //ソートは実装されていない
        // Sorting is not implmented yet.
        
        //↓辞書順に並べた結果
        /* Example from "Hi Ho Hi Ho"
         0: Hi Ho
         1: Ho
         2: Ho Hi Ho
         3:Hi Ho
         4:Hi Ho Hi Ho
         5:Ho
         6:Ho Hi Ho
         7:i Ho
         8:i Ho Hi Ho
         9:o
         A:o Hi Ho
         */
        
        //suffixArray[]中身
        /* Example from "Hi Ho Hi Ho"
         0:5
         1:8
         2:2
         3:6
         4:0
         5:9
         6:3
         7:7
         8:1
         9:10
         A:4
         */
        // printSuffixArray();
    }
    
    private int targetCompare(int i, int start, int end) {
        if(i <= -1){    //参照外なら
            return -1;
        }
        else if(i >= suffixArray.length){
            return 1;
        }
        int si = mySpace.length - suffixArray[i];
        int sj = end -start;
        int s = mySpace.length;
        if(si < s) s = si;
        if(sj < s) s = sj;
        for(int k = 0; k < s; k++){
            if(mySpace[suffixArray[i]+k] > myTarget[start + k]) return 1;   //エンド位置を通り過ぎている
            if(mySpace[suffixArray[i]+k] < myTarget[start + k]) return -1;  //まだスタート位置にたどりついていない
        }
        return 0;   //先頭文字が一致している
    }
    
    /*
    private int subByteStartIndex(int start, int end) {
        for(int i = 0; i < mySpace.length; i++){
            if(targetCompare(i,start,end) == 0){
                return i;
            }
        }
        // It returns the index of the first suffix which is equal or greater than subBytes;
        // not implemented yet;
        // For "Ho", it will return 5 for "Hi Ho Hi Ho".
        // For "Ho ", it will return 6 for "Hi Ho Hi Ho".
        return suffixArray.length;
    }
     */
    
    public int Binary_Search_Start(int top, int bottom, int start, int end){
        int middle = (top + bottom) / 2;
        if(top >= bottom){
            if(targetCompare(middle,start,end) == -1){
                System.out.println("test");
                return 0;   //ターゲットが見つからない場合
            }
            else{
                return middle;
            }
        }
        else{
            if(targetCompare(middle,start,end) == 0){
                if(middle == 0){    //middleが先頭まで来た時用の処理
                    return middle;
                }
                else if(targetCompare(middle-1,start,end) == -1){
                    return middle;
                }
                else{
                    return Binary_Search_Start(top, middle-1, start, end);
                }
            }
            else if(targetCompare(middle,start,end) == -1){
                return Binary_Search_Start(middle + 1, bottom, start, end);
            }
            else{   // if(targetCompare(middle,start,end) == 1)
                if(targetCompare(middle-1,start,end) == -1){    //ターゲットが見つからない場合
                    return 0;
                }
                else{
                    return Binary_Search_Start(top, middle, start, end);
                }
            }
        }
    }
    
    private int subByteStartIndex(int start, int end) {
        return Binary_Search_Start(0, suffixArray.length-1, start, end);
    }

    /*
    private int subByteEndIndex(int start, int end) {
        for(int i = 0; i < mySpace.length; i++){
            if(targetCompare(i,start,end) == 1){
                return i;
            }
        }
        // It returns the next index of the first suffix which is greater than subBytes;
        // not implemented yet
        // For "Ho", it will return 7 for "Hi Ho Hi Ho".
        // For "Ho ", it will return 7 for "Hi Ho Hi Ho".
        return suffixArray.length;
    }
    */
    
    public int Binary_Search_End(int top, int bottom, int start, int end){
        int middle = (top + bottom) / 2;
        if(top >= bottom && middle == suffixArray.length-1){
            if(targetCompare(middle,start,end) == 0){   //middleが末尾&ターゲットも末尾の場合
                return middle+1;
            }
            else{   //ターゲットがない場合
                return 0;
            }
        }
        else{
            if(targetCompare(middle,start,end) == 1){
                if(targetCompare(middle-1,start,end) == 0){
                    return middle;
                }
                else if(targetCompare(middle-1,start,end) == -1){
                    return 0;
                }
                else{
                    return Binary_Search_End(top, middle-1, start, end);
                }
            }
            else if(targetCompare(middle,start,end) == 0){
                if(middle == suffixArray.length-1){
                    return middle+1;
                }
                else{
                    return Binary_Search_End(middle + 1, bottom, start, end);
                }
            }
            else{
                return Binary_Search_End(middle + 1, bottom, start, end);
            }
        }
    }
    
    private int subByteEndIndex(int start, int end) {
        return Binary_Search_End(0, suffixArray.length-1, start, end);
    }
     
    public int subByteFrequency(int start, int end) {
        // This method could be defined as follows though it is slow.
        /*
         int spaceLength = mySpace.length;
         int count = 0;
         for(int offset = 0; offset< spaceLength - (end - start); offset++) {
             boolean abort = false;
             for(int i = 0; i< (end - start); i++) {
                 if(myTarget[start+i] != mySpace[offset+i]) {
                     abort = true;
                     break;
                 }
             }
             if(abort == false) {
                 count++;
             }
         }
         */
        for(int i = 0; i < mySpace.length; i++){
            System.out.println(targetCompare(i,start,end));
        }
        int first = subByteStartIndex(start,end);
        int last1 = subByteEndIndex(start, end);
        // inspection code
         for(int k=start;k<end;k++) {
             System.out.write(myTarget[k]);
         }
         System.out.printf(": first=%d last1=%d\n", first, last1);
        return last1 - first;
    }
    
    public void setTarget(byte [] target) {
        myTarget = target;
        if(myTarget.length>0) targetReady = true;
    }
    
    public int frequency() {
        if(targetReady == false) return -1;
        if(spaceReady == false) return 0;
        return subByteFrequency(0, myTarget.length);
    }
    
    public static void main(String[] args) {
        Frequencer frequencerObject;
        try {
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
            frequencerObject.setTarget("guug".getBytes());
            int result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+" ");
            if(4 == result) { System.out.println("OK"); }
            else {System.out.println("WRONG"); }
        }
        catch(Exception e) {
            System.out.println("STOP");
        }
    }
}
