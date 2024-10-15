package ph.edu.dlsu.enlistment;

import java.math.BigDecimal;
import java.util.*;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.noNullElements;

class Student {
        // make immutable
        private final int studentNo;
        private final Collection<Section> sections = new HashSet<>();
        private final Collection<Subject> completedSubjects = new HashSet<>();
        private final int maxUnits = 24;
        private final Programs degreeProgram;

        Student(int studentNo, Collection<Section> sections, Collection<Subject> completedSubjects, Programs degreeProgram) {
            isTrue(studentNo >= 0, "studentNo must be non-negative; was " + studentNo);
            Objects.requireNonNull(sections);
            Objects.requireNonNull(completedSubjects);
            this.studentNo = studentNo;
            this.sections.addAll(sections);

            this.completedSubjects.addAll(completedSubjects);
            this.degreeProgram = degreeProgram;
        }

        void enlist(Section newSection) {
            Objects.requireNonNull(newSection);
            Collection<Section> tempSections = new HashSet<>(sections);
            int totalUnits = tempSections.stream().mapToInt(section -> (int)section.getSubject().getUnits()).sum();
            if (totalUnits + newSection.getSubject().getUnits() > maxUnits) {
                throw new IllegalStateException("Cannot enlist in more than " + maxUnits + " units");
            }
            sections.forEach(currSection -> currSection.checkConflict(newSection));
            newSection.getSubject().checkPrerequisites(completedSubjects);

            newSection.enlistStudent(); // check the room capacity
            isTrue(newSection.getSubject().checkDegreeProgram(degreeProgram), "cannot enlist in section outside degree program: " + newSection.toString());
            sections.add(newSection);
        }


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

        /*
            A student can request to be assessed, which is simply a request for total amount of money that the student will need to pay. It is computed as follows:
            Each unit is ₱2,000
            Laboratory subjects have an additional ₱1,000 laboratory fee per subject
            Miscellaneous fees are ₱3,000
            Value Added Tax (VAT) is 12%
         */

        BigDecimal studentTotalAssessment(){
            BigDecimal TotalAssessment = new BigDecimal(0);
            for(Section section : sections )
            {
                TotalAssessment = TotalAssessment.add(section.getSubject().getAssessment());
            }
            TotalAssessment = TotalAssessment.add(BigDecimal.valueOf(3000)).multiply(BigDecimal.valueOf(1.12));
            return TotalAssessment;
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


