import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class facialRecognitionApp {

    public static final int NUM_PEOPLE = 5;
    public static final int NUM_MEASUREMENTS = 6;
    public static final int NUM_RATIOS = 15;

    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);

        String[] names = new String[NUM_PEOPLE];
        double[][] measurements = new double[NUM_PEOPLE][NUM_MEASUREMENTS];
        double[][] ratios = new double[NUM_PEOPLE][NUM_RATIOS];

        // Read known faces from file
        System.out.print("Enter input file name: ");
        String fileName = console.nextLine();

        Scanner input = new Scanner(new File(fileName));

        for (int i = 0; i < NUM_PEOPLE; i++) {
            names[i] = input.next();
            for (int j = 0; j < NUM_MEASUREMENTS; j++) {
                measurements[i][j] = input.nextDouble();
            }
            ratios[i] = computeRatios(measurements[i]);
        }

        input.close();

        //  read faces 
        double[] mysteryMeasurements = new double[NUM_MEASUREMENTS];
        System.out.println("\nEnter the 6 measurements for the mystery picture:");

        System.out.print("A (top of head to bottom of chin): ");
        mysteryMeasurements[0] = console.nextDouble();

        System.out.print("B (left ear to right ear): ");
        mysteryMeasurements[1] = console.nextDouble();

        System.out.print("C (between eyes to top of head): ");
        mysteryMeasurements[2] = console.nextDouble();

        System.out.print("D (left eye center to right eye center): ");
        mysteryMeasurements[3] = console.nextDouble();

        System.out.print("E (top of nose to bottom of nose): ");
        mysteryMeasurements[4] = console.nextDouble();

        System.out.print("F (bottom of chin to middle of mouth): ");
        mysteryMeasurements[5] = console.nextDouble();

        double[] mysteryRatios = computeRatios(mysteryMeasurements);

        // compare mystery faces to known faces using sum of squares % difference
        double smallestDifference = computeDifference(ratios[0], mysteryRatios);
        int bestMatchIndex = 0;

        System.out.println("\nSum of squares % difference results:");
        System.out.printf("%s: %.6f%n", names[0], smallestDifference);

        for (int i = 1; i < NUM_PEOPLE; i++) {
            double difference = computeDifference(ratios[i], mysteryRatios);
            System.out.printf("%s: %.6f%n", names[i], difference);

            if (difference < smallestDifference) {
                smallestDifference = difference;
                bestMatchIndex = i;
            }
        }

        System.out.println("\nThe mystery picture is most likely: " + names[bestMatchIndex]);
    }

    //  compute 15 ratios from the 6 measurements
    public static double[] computeRatios(double[] m) {
        double[] r = new double[NUM_RATIOS];

        double A = m[0];
        double B = m[1];
        double C = m[2];
        double D = m[3];
        double E = m[4];
        double F = m[5];

        int index = 0;

        r[index++] = A / B;
        r[index++] = A / C;
        r[index++] = A / D;
        r[index++] = A / E;
        r[index++] = A / F;
        r[index++] = B / C;
        r[index++] = B / D;
        r[index++] = B / E;
        r[index++] = B / F;
        r[index++] = C / D;
        r[index++] = C / E;
        r[index++] = C / F;
        r[index++] = D / E;
        r[index++] = D / F;
        r[index++] = E / F;

        return r;
    }

    // Computes the sum of squares % difference
    public static double computeDifference(double[] originalRatios, double[] mysteryRatios) {
        double sum = 0.0;

        for (int i = 0; i < NUM_RATIOS; i++) {
            double percentDifference = (mysteryRatios[i] - originalRatios[i]) / originalRatios[i];
            sum += percentDifference * percentDifference;
        }

        return sum;
    }
}