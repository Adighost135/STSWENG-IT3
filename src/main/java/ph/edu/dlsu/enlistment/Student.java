package ph.edu.dlsu.enlistment;

import java.util.*;
import java.math.*;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.noNullElements;

class Student {
    // make immutable
    private final int studentNo;
    private final Collection<Section> sections = new HashSet<>();
    private final Collection<Subject> completedSubjects = new HashSet<>();

    private static final BigDecimal UNIT_COST = new BigDecimal("2000");
    private static final BigDecimal LABORATORY_FEE = new BigDecimal("1000");
    private static final BigDecimal MISCELLANEOUS_FEES = new BigDecimal("3000");
    private static final BigDecimal VAT = new BigDecimal("0.12");
//    private static final int MIN_SECTIONS = 3;
//    private static final int MAX_SECTIONS = 7;

    Student(int studentNo, Collection<Section> sections, Collection<Subject> completedSubjects) {
        isTrue(studentNo >= 0, "studentNo must be non-negative; was " + studentNo);
        Objects.requireNonNull(sections);
        Objects.requireNonNull(completedSubjects);
        this.studentNo = studentNo;
        this.sections.addAll(sections);

        this.completedSubjects.addAll(completedSubjects);
    }

    void enlist(Section newSection) {
        Objects.requireNonNull(newSection);
        // isTrue(!sections.contains(section), "cannot enlist in same section: " + section);
//         if(newSection.subject)
        sections.forEach(currSection -> currSection.checkConflict(newSection));
        sections.forEach(currSection -> currSection.getSubject().checkPrerequisites(completedSubjects));
        newSection.enlistStudent(); // check the room capacity


//        isTrue(sections.size() < MAX_SECTIONS,
//                "Student cannot enlist in more than " + MAX_SECTIONS + " sections");

        sections.add(newSection);
    }

    // boolean canEnlist(Subject)

    void cancelEnlist(Section cancelSection){
        Objects.requireNonNull(cancelSection);
        if(!sections.contains(cancelSection)){
            throw new IllegalArgumentException("Student is not enlisted in this section, section " + cancelSection);
        }
        cancelSection.cancelEnlistment();
        sections.remove(cancelSection);
    }

    Collection<Section> getSections() {
        return new ArrayList<>(sections);
    }

    Collection<Subject> getCompletedSubjects() {
        return new ArrayList<>(completedSubjects);
    }

//    boolean studentAssessment() {
//        // Validation if ever the student has literally no sections
//        isTrue(sections.size() >= MIN_SECTIONS, "WARNING! Student must be enrolled in at least " + MIN_SECTIONS + " sections for assessment");
//        //Validation if ever the student has reach the max sections
//        isTrue(sections.size() <= MAX_SECTIONS, "WARNING! Student cannot be enrolled in more than " + MAX_SECTIONS + " sections");
//        return true;
//    }


    BigDecimal calculateTotalAssessment() {
        BigDecimal totalFee = BigDecimal.ZERO;
        int totalUnits = 0;
        int labSubjectCount = 0;

        // Loop through sections to get the total sum
        for (Section section : sections) {
            totalUnits += section.getSubject().getUnits();
            // Access the subject from the section and check if it's a lab subject
            if (section.getSubject().isLaboratory()) {
                labSubjectCount++;
            }
        }

        // cost for units
        totalFee = totalFee.add(UNIT_COST.multiply(BigDecimal.valueOf(totalUnits)));

        // lab fees
        totalFee = totalFee.add(LABORATORY_FEE.multiply(BigDecimal.valueOf(labSubjectCount)));

        // miscellaneous fees
        totalFee = totalFee.add(MISCELLANEOUS_FEES);

        // apply VAT (12%)
        BigDecimal vatAmount = totalFee.multiply(VAT);
        totalFee = totalFee.add(vatAmount);

        //rounding for two decimal places
        totalFee = totalFee.setScale(2, RoundingMode.HALF_UP);

        return totalFee;
    }

    @Override
    public String toString(){
        return "Student# " + studentNo;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;

        return studentNo == student.studentNo;
    }

    @Override
    public int hashCode() {
        return studentNo;
    }
}


