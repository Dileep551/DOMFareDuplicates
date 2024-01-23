import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FindAndSortDuplicatesTest {


    @Test
    void testSortedDuplicatesToFile() throws IOException {

        //Given
        String zipFilePath1 = "DOM_FILES/DOMFARE_Sample.D220314.T1300.zip";
        String zipFilePath2 = "DOM_FILES/DOMFARE_Sample.D220314.T2000.zip";
        String outputFilePath = "DOM_FILES/sorted_duplicates_sample.txt";
        String actualFilePath = "DOM_FILES/sorted_duplicates_actual_sample.txt";


        //When
        FindAndSortDuplicates.sortedDuplicatesToFile(zipFilePath1, zipFilePath2, outputFilePath);

        List<String> expectedLines = Files.readAllLines(Path.of(actualFilePath));
        List<String> actualLines = Files.readAllLines(Path.of(outputFilePath));

        //Then
        assertEquals(expectedLines, actualLines);


    }

    @Test
    void testFindDuplicates() {
        // Create sample FareRecord lists for testing
        Set<FareRecord> records1 = Set.of(new FareRecord(12, "AA", "ABQ", "DEN",
                        "KAX2LFFM", 2, 11),
                new FareRecord(12, "AA", "DEN", "ABQ",
                        "KAX2LFFM", 2, 11));
        Set<FareRecord> records2 = Set.of(new FareRecord(12, "AA", "ABQ", "DEN",
                        "KAX2LFFM", 2, 11),
                new FareRecord(12, "AA", "DEN", "ABQ",
                        "KAX2LFFA", 2, 11));

        Set<FareRecord> duplicates = FindAndSortDuplicates.findDuplicates(records1, records2);

        assertEquals(1, duplicates.size());
    }

    @Test
    void testSortDuplicates() {

        /*
        FareRecord[tariff=30, carrier=UA , origin=ABE  , destination=HNL  , fareClass=QAA5HFEM, link=2, sequence=7]
        FareRecord[tariff=30, carrier=UA , origin=ABE  , destination=HNL  , fareClass=QAA5HFEN, link=2, sequence=7]
         */

        Set<FareRecord> duplicates = Set.of(
                new FareRecord(30, "UA", "ABE", "HNL",
                        "QAA5HFEO", 2, 7),
                new FareRecord(30, "UA", "ABE", "HNL",
                        "QAA5HFEN", 2, 7),
                new FareRecord(30, "UA", "ABE", "HNL",
                        "QAA5HFEM", 2, 7));

        List<FareRecord> sortedExpected = List.of(
                new FareRecord(30, "UA", "ABE", "HNL",
                        "QAA5HFEM", 2, 7),
                new FareRecord(30, "UA", "ABE", "HNL",
                        "QAA5HFEN", 2, 7),
                new FareRecord(30, "UA", "ABE", "HNL",
                        "QAA5HFEO", 2, 7));

        List<FareRecord> sortedDuplicates = FindAndSortDuplicates.sortDuplicates(duplicates);


        assertEquals(sortedExpected, sortedDuplicates);
    }


}