import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {



        String zipFilePath1 = "DOM_FILES/DOMFARE.D220314.T1300.zip";
        String zipFilePath2 = "DOM_FILES/DOMFARE.D220314.T2000.zip";
        String outputFilePath = "DOM_FILES/sorted_duplicates_original.txt";

        FindAndSortDuplicates.sortedDuplicatesToFile(zipFilePath1, zipFilePath2, outputFilePath);


    }
}