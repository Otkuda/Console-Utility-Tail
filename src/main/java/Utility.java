import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Utility {
    @Option(name = "-o")
    private String outputFile;

    @Option(name = "-c", forbids = {"-n"})
    private int lastSymbols;
    private boolean isSym;

    @Option(name = "-n", forbids = {"-c"})
    private int lastStrings;
    private boolean isStr;

    @Argument()
    private ArrayList<String> files = new ArrayList<>();


    public static void main(String[] args) throws IOException {
        new Utility().launch(args);
    }

    private void launch(String[] args) throws IOException {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e){
            System.err.println(e.getMessage());
            System.err.println("java SampleMain [options...] arguments...");
            parser.printUsage(System.err);
            System.err.println();
            return;
        }

        if (!files.isEmpty()) {
            for (String name : files) {
                tail(name, outputFile);
            }
        } else tail(null, outputFile);
    }

    private void tail(String inputName, String outputName) throws IOException {
        StringBuilder input = new StringBuilder();
        if (inputName == null) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Type input text or type \"EOF\" to finish");
            String str = sc.nextLine();
            while (sc.hasNext() && !str.equals("EOF")) {
                input.append(str).append("\n");
                str = sc.nextLine();
                if (str.equals("EOF")) break;
            }
        } else {
            try(BufferedReader reader = new BufferedReader(new FileReader(inputName))) {
                String str = reader.readLine();
                while (str != null) {
                    input.append(str).append("\n");
                    str = reader.readLine();
                }
            }
        }

        String result;
        if (lastStrings != 0) result = getLastStrings(input, lastStrings);
        else if (lastSymbols != 0) result = getLastSymbols(input, lastSymbols);
        else {
            if (input.toString().split("\n").length <= 10) result = input.toString();
            else {
                String[] arr = input.toString().split("\n");
                result = String.join("\n", Arrays.stream(arr).skip(arr.length - 10).toArray(String[]::new));
            }
        }

        if (outputName == null) {
            System.out.printf("%s\n%s", inputName, result);
        } else {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputName))){
                writer.write(result);
            }
        }
    }
    public String getLastStrings(StringBuilder in, int n) {
        String[] array = in.toString().trim().split("\n");
        String[] res;
        res = Arrays.stream(array).skip(array.length - n).toArray(String[]::new);
        return String.join("\n", res);
    }

    public String getLastSymbols(StringBuilder in, int n) {
        String[] array = in.toString().trim().split("");
        return String.join("", Arrays.stream(array).skip(array.length - n).toArray(String[]::new));
    }

}
