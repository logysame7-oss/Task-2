import java.util.*;

/*
 TASK: Cinema Seat Booking - a HashMap with your own class as the key

 key   = Seat   (row + number, for example B5)
 value = String (name of the customer who booked it)
*/
class Task_CinemaBooking {

    static class Seat {
        final char row;     // 'A' to 'E'
        final int number;   // 1 to 6

        Seat(char row, int number) {
            this.row = row;
            this.number = number;
        }

        @Override
        public String toString() {
            return "" + row + number;
        }

        // TODO 1: equals() - same seat when row AND number are the same
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Seat)) return false;
            Seat other = (Seat) o;
            return row == other.row && number == other.number;
        }

        // TODO 2: hashCode() - built from the same fields as equals()
        @Override
        public int hashCode() {
            return 31 * row + number;
        }
    }

    public static void main(String[] args) {

        HashMap<Seat, String> bookings = new HashMap<>();
        HashSet<Seat> broken = new HashSet<>();

        // Part A
        bookings.put(new Seat('A', 1), "Ali");
        bookings.put(new Seat('A', 2), "Sara");
        bookings.put(new Seat('B', 5), "Omar");
        bookings.put(new Seat('C', 3), "Mona");
        System.out.println("Bookings:           " + bookings);

        // Part B
        System.out.println("Who booked C3:      " + bookings.get(new Seat('C', 3)));

        // Part C
        System.out.println("Is A2 taken:        " + bookings.containsKey(new Seat('A', 2)));
        System.out.println("Is E6 taken:        " + bookings.containsKey(new Seat('E', 6)));

        // Part D
        String existing = bookings.putIfAbsent(new Seat('B', 5), "Youssef");
        if (existing == null) {
            System.out.println("Youssef -> B5:      booked");
        } else {
            System.out.println("Youssef -> B5:      refused, taken by " + existing);
        }

        // Part E
        bookings.remove(new Seat('A', 1));
        bookings.put(new Seat('D', 2), "Ali");
        System.out.println("After Ali moved:    " + bookings);

        // Part F
        broken.add(new Seat('E', 1));
        broken.add(new Seat('E', 2));
        if (broken.contains(new Seat('E', 1))) {
            System.out.println("Hana -> E1:         refused, seat is broken");
        } else {
            bookings.put(new Seat('E', 1), "Hana");
            System.out.println("Hana -> E1:         booked");
        }

        // Part G
        System.out.println("Seats booked:       " + bookings.size());

        // GIVEN
        System.out.println();
        printChart(bookings, broken);
    }

    // GIVEN - do not change.  X = booked   # = broken   . = free
    static void printChart(HashMap<Seat, String> bookings, HashSet<Seat> broken) {
        System.out.println("     1  2  3  4  5  6");
        for (char row = 'A'; row <= 'E'; row++) {
            System.out.print(row + "  ");
            for (int number = 1; number <= 6; number++) {
                Seat seat = new Seat(row, number);
                if (bookings.containsKey(seat)) {
                    System.out.print("  X");
                } else if (broken.contains(seat)) {
                    System.out.print("  #");
                } else {
                    System.out.print("  .");
                }
            }
            System.out.println();
        }
    }
}

/*
 QUESTIONS AND ANSWERS

 STEP 2: What was wrong before equals() and hashCode()?
 Each new Seat('B', 5) counted as a different key, even with the same row
 and number. So get(C3) returned null, containsKey(A2) was false, Youssef
 could book B5, Hana could book the broken E1, and the map held 7 entries
 instead of 4.

 Q1. Why did we have to override hashCode() and equals()?
 HashMap uses hashCode() to find the bucket and equals() to find the key in
 that bucket. By default both just compare memory addresses, so a new Seat
 with the same row and number never matches. Overriding them makes seats
 with the same row and number count as one key.

 Q2. Delete hashCode() but keep equals(). What happens, and why?
 Lookups break again. Equal seats get different default hash codes, so
 HashMap checks the wrong bucket and never reaches equals(). The rule: equal
 objects must have the same hashCode().

 Q3. Why are row and number final? What if the number changed after the seat
     was put in the map?
 The seat is stored in a bucket chosen by its hash at that moment. If the
 number changed, the hash would change too, so get() or remove() would look
 in a different bucket and miss the booking. final prevents that.

 Q4. What happens if equals(Object o) becomes equals(Seat o)?
 With @Override it won't compile, since it no longer overrides
 equals(Object). Without @Override it compiles, but HashMap still calls
 equals(Object), which is the default one, so the old bugs return.

 BONUS. What happens if Seat is replaced with: record Seat(char row, int number) {}
 It still works and gives correct results. A record generates equals(),
 hashCode() and toString() from its fields, and the fields are final. Only
 the printing changes: Seat[row=A, number=1] instead of A1. Records need
 Java 16 or newer.
*/
