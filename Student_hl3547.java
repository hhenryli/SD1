import java.util.Arrays;
import java.util.List;

public class Student_hl3547 implements Student {

  // helper class to store and rank schools based on a calculated score
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

  // estimating P(your score > a random opponent score) using Monte Carlo simulation
  private double pBeatOne(double x, double S, double W) {
    int trials = 10000;
    int wins = 0;
    for (int i = 0; i < trials; i++) {
      // generate a random opponent's aptitude and their synergy with the school
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

    int opponents = 9;

    School[] preferences = new School[schools.size()];
    for (int i = 0; i < schools.size(); i++) {
      double q = schools.get(i);
      double syn = synergies.get(i);
      
      // the payoff you receive if matched
      double myPref = q + syn;
      // how the university ranks you
      double myScoreAtSchool = aptitude + syn;

      // the likelihood your score is the highest among applicants
      double pBeat = pBeatOne(myScoreAtSchool, S, W);
      double pAdmitted = Math.pow(pBeat, opponents);

      // expected value
      double score = myPref * pAdmitted;
      preferences[i] = new School(i, score);
    }

    // sorting all N schools with the calculated expected value
    Arrays.sort(preferences);
    
    int[] ret = new int[10];
    for (int i = 0; i < 10; i++) {
      ret[i] = preferences[i].index;
    }
    return ret;
  }
}