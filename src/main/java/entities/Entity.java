package entities;


public interface Entity {
    //Interface bruges for at vi kan refaktorere vores testkode i bl.a persist, som kan tage en Entity som
    //parameter fremfor, at der skulle laves en persist for hvert enkelt Entity.
    //getId() er eneste fælles metode de deler.
    Integer getId();
}
