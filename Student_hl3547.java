import java.util.Arrays;
import java.util.List;

public class Student_hl3547 implements Student {

  private class School implements Comparable<School> {
    public School(int i, double q) {
      index = i;
      quality = q;
    }

    private int index;
    private double quality;

    public int compareTo(School n) {
      int ret = Double.compare(n.quality, quality);
      return (ret == 0) ? (Integer.compare(index, n.index)) : ret;
    }
  }

  // P(one random opponent scores less than x), where opponent = Uniform[0,S] + Uniform[0,W]
  private double pBeatOne(double x, double S, double W) {
    int trials = 10000;
    int wins = 0;
    for (int i = 0; i < trials; i++) {
        double opponentScore = Math.random() * S + Math.random() * W;
        if (x > opponentScore) wins++;
    }
    return (double) wins / trials;
}

  public int[] getApplications(
      int N,
      double S,
      double T,
      double W,
      double aptitude,
      List<Double> schools,
      List<Double> synergies) {

    // Expected competitors at each school = 10 (since N students * 10 apps / N schools)
    // You need to beat all other ~9 applicants
    int opponents = 9;

    School[] preferences = new School[schools.size()];
    for (int i = 0; i < schools.size(); i++) {
      double q = schools.get(i);
      double syn = synergies.get(i);

      double myPref = q + syn;                    // how much we want this school
      double myScoreAtSchool = aptitude + syn;    // how the school ranks us

      double pBeat = pBeatOne(myScoreAtSchool, S, W);
      double pAdmitted = Math.pow(pBeat, opponents);

      double score = myPref * pAdmitted;
      preferences[i] = new School(i, score);
    }

    Arrays.sort(preferences);
    int[] ret = new int[10];
    for (int i = 0; i < 10; i++) {
      ret[i] = preferences[i].index;
    }
    return ret;
  }
}