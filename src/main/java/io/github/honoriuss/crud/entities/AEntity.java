package io.github.honoriuss.crud.entities;

/**
 * @author H0n0riuss
 */
public abstract class AEntity {
    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

}
