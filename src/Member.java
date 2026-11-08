public class Member {
    private final String memberId;
    private final String name;
    private final String contactNumber;
    private int points;

    public Member(String memberId, String name, String contactNumber) {
        this.memberId = memberId;
        this.name = name;
        this.contactNumber = contactNumber;
        points = 0;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public int getPoints() {
        return points;
    }

    public boolean addPoints(int earnedPoints) {
        if (earnedPoints < 0) {
            return false;
        } else if (earnedPoints > Integer.MAX_VALUE - points) {
            return false;
        } else {
            points = points + earnedPoints;
            return true;
        }
    }

    @Override
    public String toString() {
        return memberId + ": " + name + ", contact: " + contactNumber
                + ", points: " + points;
    }
}
