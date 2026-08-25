package scoutcampus.interfaces;

/**
 * INTERFACE: any entity that can apply to a project must implement this.
 * Currently only Student implements it, but it is designed so that, e.g.,
 * a future "Alumni" or "TeamProxy" class could implement it too.
 */
public interface Applicant {
    void apply();
}
