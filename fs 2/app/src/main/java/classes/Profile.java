package classes;

public class Profile {

    private String firstName, lastName, password, email, school, resume, schoolYear;
    private DOB dob;
    private boolean foodAllergies;
    private int foodPoints; // 1 Freshman, 2 Sophomore, 3 Junior, 4 Senior
    private boolean canEat; //This does not show up in registration
    private boolean checkedIn = false;
    private static Profile singleton = new Profile();

    public static Profile getInstance() {
        return singleton;
    }


    /*SETTERS*/

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDOB(int month, int day, int year) {
        this.dob = new DOB(month, day, year);
    }

    public void setFoodAllergies(boolean foodAllergies) {
        this.foodAllergies = foodAllergies;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setSchoolYear(String schoolYear) { this.schoolYear = schoolYear; }

    public void setCanEat(boolean canEat) {
        this.canEat = canEat;
    }

    public void setCheckedIn(boolean checkIn) {//THhis will be set later.
        this.checkedIn = checkIn; }

    /**
     * I'm not sure how to exactly implement this thing.
     */
    public void setResume(String resume) {
        this.resume = resume;
    }



    /*Getters*/
    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {return this.lastName;}
    public String getDOB() {return this.dob.toString();}
    public boolean hasFoodAllergies() {return this.foodAllergies;}
    public String getSchool() {return this.school;}
    public String getSchoolYear() {return this.schoolYear;}

}