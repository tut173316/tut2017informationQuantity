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
    //開始位置の値？
    //並び順は配列として保存？
    
    // The variable, "suffixArray" is the sorted array of all suffixes of mySpace.
    // Each suffix is expressed by a interger, which is the starting position in mySpace.
    // The following is the code to print the variable
    private void printSuffixArray() {
        if(spaceReady) {
            for(int i=0; i< mySpace.length; i++) {
                //suffixArrayから開始位置を取得
                int s = suffixArray[i];
                
                //開始位置から最後までの文字列を出力
                for(int j=s;j < mySpace.length;j++) {
                    System.out.write(mySpace[j]); }
                System.out.write('\n'); }
        }
    }
    
    private int suffixCompare(int i, int j) {
        //　２つのsuffixを辞書順で比較
        // comparing two suffixes by dictionary order.
        //　iとjはsuffix_iとsuffix_jを意味する
        // i and j denoetes suffix_i, and suffix_j
        
        //i,jはsuffixのインデックス？
        //arrayの範囲指定コピーはSystem.arraycopy(from[], int start, to[], int start, int long)

        // if suffix_i > suffix_j, it returns 1
        // if suffix_i < suffix_j, it returns -1
        // if suffix_i = suffix_j, it returns 0;
        
        //まだ実装されていないが、suffixArrayを作成する必要がある
        // It is not implemented yet,
        // It should be used to create suffix array.
        
        //辞書順の例
        // Example of dictionary order
        //文字コードで比較
        //先頭が同じなら次の要素で比較
        //接頭辞？が同じなら長いほうが大きい
        // "i"    < "o"    : compare by code
        // "Hi"   < "Ho"   ; if head is same, compare the next element
        // "Ho"   < "Ho "  ; if the prefix is identical, longer string is big
        
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
    
    public void setSpace(byte []space) {
        mySpace = space;
        if(mySpace.length>0) spaceReady = true;
        int temp = 0;
        suffixArray = new int[space.length];
        // put all suffixes in suffixArray. Each suffix is expressed by one interger.
        for(int i = 0; i< space.length; i++) {
            suffixArray[i] = i;
        }
        for(int i = 0; i < mySpace.length-1; i++){
            for(int k = i+1; k < mySpace.length; k++){
                if(suffixCompare(i,k)==1){
                    temp = suffixArray[i];
                    suffixArray[i] = suffixArray[k];
                    suffixArray[k] = temp;
                }
            }
        }
        
        
        //String sb
        
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
        
        // It is called from subBytesStarIndex, adn subBytesEndIndex.
        // "start" and "end" are same as in subByteStartIndex, and subByteEndIndex **
        // target_start_end is subBytes(start, end) of target **
        // comparing suffix_i and target_start_end by dictonary order with limitationof length; ***
        // if the beginning of suffix_i matches target_start_end, and suffix is longer than target it returns 0;
        // if suffix_i > target_start_end it return 1; **
        // if suffix_i < target_start_end it return -1 **
        //
        // It should be used to search the apropriate index of some suffix.
        // Example of search
        // suffix target
        // "o"  > "i"
        // "o"  < "z"
        // "o"  = "o"
        // "o"  < "oo"
        // "Ho" > "Hi"
        // "Ho" < "Hz"
        // "Ho" = "Ho"
        // "Ho" < "Ho" :"Ho"isnotintheheadofsuffix"Ho"
        // "Ho"  = "H" : "H" is in the head of suffix "Ho"
    }
    
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
    
    public int subByteFrequency(int start, int end) {
        // This method could be defined as follows though it is slow.
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
        int first = subByteStartIndex(start,end);
        int last1 = subByteEndIndex(start, end);
        // inspection code
         for(int k=start;k<end;k++) { System.out.write(myTarget[k]); }
         System.out.printf(": first=%d last1=%d\n", first, last1);
         //
        return last1 - first;
    }
    
    public void setTarget(byte [] target) {
        myTarget = target; if(myTarget.length>0) targetReady = true;
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
            frequencerObject.setTarget("Ho".getBytes());
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
