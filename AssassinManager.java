// this class manages the AssassinNode class. On top of each function is a short sentence 
// describing it 

package hw3_ojm05;
import java.util.ArrayList;

class AssassinManager{

    private AssassinNode headKillRing;
    private AssassinNode headGraveyard;

    // non-default constructor that takes an array list of strings.
    // throws exception if input is inapt
    public AssassinManager(ArrayList<String> names){
        if (names==null || names.size()==0){
            throw new IllegalArgumentException
                ("argument provided is inapt. It must be non-null and > 0.");
        }
        else {
            headKillRing = new AssassinNode(names.get(0));
            AssassinNode current = headKillRing;
            for(int i=1;i<names.size();i++){
                current.next = new AssassinNode(names.get(i));
                current = current.next;
            }
        }
    }

    // function that prints the kill ring. 
    public void printKillRing(){
        if (this.isGameOver()==true){}
        else{
            AssassinNode current = headKillRing;
            while(current.next!=null){
                System.out.println(current.name+" is stalking "+current.next.name);
                current = current.next;
            }
            System.out.println(current.name+" is stalking "+headKillRing.name);
        }
    }

    // prints elements in graveyard. The function printKillRing and printGraveyard cannot
    // be shorthened with a common function since (1) they both iterate with different 
    // while loop conditions, (2) they are printing different objects and (3) these objects are
    // at different areas of the linked list. It would thus make the function more complicated
    public void printGraveyard(){
        AssassinNode current = headGraveyard;
        while (current!=null){
            System.out.println(current.name+" was killed by "+current.killer);
            current = current.next;
        }
    }  

    // Below reduces redundancy between iterateListBool and, for the kill function, finds
    // the killer of the killed. Takes in head of list (whether graveyard or killRing),
    // name to be found. Returns the either the killer (if not head) or the killed (if head) 
    // or null (if name not found) 
    public AssassinNode iterateListString(AssassinNode head, String name){
        if (head.name.equalsIgnoreCase(name)) return head;
        while (head.next!=null){
            if (head.next.name.equalsIgnoreCase(name)) {
                return head;
            }
            head = head.next;
        }
        return null;
    }

    // this function reduce redundancy between the killRingContains and graveyardContains.
    // Parameters same as above. Returns boolean expression: element present (true) or not.
    public boolean iterateListBool(AssassinNode head, String name){
        if (this.iterateListString(head,name) == null) return false;
        else return true;
    }
    //checks if name is in killRing using iterateListBool function. returns boolean as above.
    public boolean killRingContains(String name){
        return iterateListBool(headKillRing, name);
    }

    // checks if name is in graveyard using iterateListBool function. returns boolean as above.
    public boolean graveyardContains(String name){
        if (headGraveyard==null) return false;
        return iterateListBool(headGraveyard,name);
    }
    
    // checks if game is over. Returns boolean.
    public boolean isGameOver(){
        if (headKillRing.next == null) return true;
        else return false;
    }

    // checks whether there is a winner and returns how that winner is named.
    // returns null if no winner
    public String winner(){
        if (!this.isGameOver()) return null;
        else return headKillRing.name;
    }

    // function that kills a player. Takes in the player to be killed. Adds player to graveyard.
    // removes player from initial linked list while keeping list intact
    public void kill (String name){

        // retrives the node right before the one killed except if
        // head is to be killed, then it returns head 
        AssassinNode previousKill = this.iterateListString(headKillRing,name);

        // checks for exceptions: game is over / name not present in list
        if (this.isGameOver()) {
            throw new IllegalStateException("Cannot kill anyone. Game is over.");
        }
        else if (previousKill == null) {
            throw new IllegalArgumentException("Argument is not present in Linked List.");
        }
        else{
            // checks if element to be killed is head (it has to be dealt with differently)
            if (!previousKill.next.name.equalsIgnoreCase(name)) {
                headKillRing = previousKill.next;
                // iterates over list -> gets last element -> assigns as killer.
                previousKill.killer = this.lastElement().name;
                //adds to graveyard. Input is person killed.
                this.addingGraveyard(previousKill);
            }
            // All nodes other than head get treated the same way (but differently than head)
            else {
                AssassinNode killed = previousKill.next;
                killed.killer = previousKill.name;
                previousKill.next = previousKill.next.next;
                this.addingGraveyard(killed);
            }
        }
    }
    // Returns last element of list (used when head is killed). Cannot be simplified using other
    // functions since it returns an AssassinNode whereas other functions return other objects
    public AssassinNode lastElement(){
        AssassinNode current = headKillRing;
        while (current.next!=null) 
            current = current.next;
        return current;
    }

    // function adds elements in the graveyard (used twice in function kill)
    // takes in as input the person killed. Returns nothing but adds person killd at front of
    // linked list for graveyard. 
    public void addingGraveyard(AssassinNode killed) {
        if (headGraveyard==null) {
            headGraveyard = killed;
            headGraveyard.next = null;
        }
                else {
                    killed.next = headGraveyard; 
                    headGraveyard = killed;
                }
    }
}