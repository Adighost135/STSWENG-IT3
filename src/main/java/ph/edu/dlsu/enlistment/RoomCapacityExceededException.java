package ph.edu.dlsu.enlistment;

public class RoomCapacityExceededException extends RuntimeException{
    public RoomCapacityExceededException(String msg){
        super(msg);
    }
}
