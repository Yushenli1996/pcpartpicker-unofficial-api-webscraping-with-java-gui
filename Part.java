/*
This is an idea for now. Since sometimes we want to do something for various parts,
instead of doing an if-else-else-else-else cascade, maybe we can use this parent class Part and
call Part.dosomething() instead. This would make the call general -> regardless of specific part.

 */

import java.util.ArrayList;

public class Part {
    public static ArrayList<Part> partlist = new ArrayList<>();
    //private String[];

    public Part() {
        partlist.add(this);

    }
}

/*
    1. create a list of links
    2. loop through the list and decide which object to create
    3. call those two funtions(get[]fromform + getrawdata) create objects (parse + create object(super))
    4. put into the partlist
    5.
*/





//Part cpu = new Part
//Part.partlist.add (cpu)

//Search
//for (Part p: partlist){
//
//        }


//getpricedata()
//getspecsdata()