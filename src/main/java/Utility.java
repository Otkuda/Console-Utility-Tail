import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private final ArrayList<String> files = new ArrayList<>();


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

        for(String name:files) {
            tail(name, outputFile);
        }

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
        if (isStr) result = getLastStrings(input);
        else if (isSym) result = getLastSymbols(input);
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
    private String getLastStrings(StringBuilder in) {
        String[] array = in.toString().split("\n");
        String[] res;
        res = Arrays.stream(array).skip(array.length - lastStrings).toArray(String[]::new);
        return String.join("\n", res);
    }

    private String getLastSymbols(StringBuilder in) {
        String[] array = in.toString().split("");
        return String.join("", Arrays.stream(array).skip(array.length - lastSymbols).toArray(String[]::new));
    }

}
