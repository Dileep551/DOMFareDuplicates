import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;



record FareRecord(Integer tariff, String carrier, String origin, String destination, String fareClass,
                  Integer link, Integer sequence) {
}

public class FindAndSortDuplicates {
    public static void sortedDuplicatesToFile(String zipFilePath1, String zipFilePath2, String outputFilePath) {

        try {
            var records1 = readFromZipFile(zipFilePath1);
            var records2 = readFromZipFile(zipFilePath2);

            var duplicates = findDuplicates(records1, records2);
            var sortedDuplicates = sortDuplicates(duplicates);

            writeToFile(sortedDuplicates, outputFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Set<FareRecord> readFromZipFile(String zipFilePath) throws IOException {
        Set<FareRecord> recordList;

        try (ZipFile zipFile = new ZipFile(zipFilePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(zipFile.entries().nextElement())))) {

            recordList = reader.lines()
                    .skip(1)
                    .map(line -> {
                        try {
                            return parseData(line);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toSet());
        }

        return recordList;
    }


    static Set<FareRecord> findDuplicates(Set<FareRecord> records1, Set<FareRecord> records2) {
        return records1.stream()
                .filter(records2::contains)
                .collect(Collectors.toSet());
    }

    static List<FareRecord> sortDuplicates(Set<FareRecord> duplicates) {
        return duplicates.stream()
                .sorted(Comparator
                        .comparing(FareRecord::tariff)
                        .thenComparing(FareRecord::carrier)
                        .thenComparing(FareRecord::origin)
                        .thenComparing(FareRecord::destination)
                        .thenComparing(FareRecord::fareClass)
                        .thenComparing(FareRecord::link)
                        .thenComparing(FareRecord::sequence))
                .collect(Collectors.toList());
    }

    private static void writeToFile(List<FareRecord> records, String filePath) throws IOException {
        List<String> lines = records.stream()
                .map(FareRecord::toString)
                .collect(Collectors.toList());

        Files.write(Paths.get(filePath), lines, StandardCharsets.UTF_8);
    }

    private static FareRecord parseData(String line) throws IOException {
        int tariff = Integer.parseInt(line.substring(0, 3));
        String carrier = line.substring(3, 6);
        String origin = line.substring(6, 11);
        String destination = line.substring(11, 16);
        String fareClass = line.substring(16, 24);
        int link = Integer.parseInt(line.substring(81, 84));
        int sequence = Integer.parseInt(line.substring(84, 89));

        return new FareRecord(tariff, carrier, origin, destination, fareClass, link, sequence);
    }
}