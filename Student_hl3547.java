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
    if (S <= 0 || W <= 0) return x > 0 ? 1.0 : 0.0;
    double lo = Math.min(S, W);
    double hi = Math.max(S, W);
    double total = S + W;
    x = Math.max(0, Math.min(x, total)); // clamp
    if (x <= lo) {
      return (x * x) / (2 * S * W);
    } else if (x <= hi) {
      return (x - lo / 2.0) / hi;
    } else {
      double diff = total - x;
      return 1.0 - (diff * diff) / (2 * S * W);
    }
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