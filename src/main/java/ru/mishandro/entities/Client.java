package ru.mishandro.entities;

import org.jetbrains.annotations.NotNull;

/**
 * An object describing the parameters of clients
 */
public class Client {
    private int id = -1;
    private final @NotNull String name;
    private final @NotNull String surname;
    private String address;
    private String passportNumber;
    private final int bankId;

    /**
     * @param name client's name
     * @param surname client's surname
     * @param bankId id of the {@link Bank} that the client belongs to
     */
    public Client(
            @NotNull String name,
            @NotNull String surname,
            int bankId) {
        this.name = name;
        this.surname = surname;
        this.bankId = bankId;
    }

    /**
     *  Copy of this {@link Client}
     */
    public Client clone() {
        Client cloneClient = new Client(name, surname, bankId);
        cloneClient.id = id;
        cloneClient.address = address;
        cloneClient.passportNumber = passportNumber;

        return cloneClient;
    }

    /**
     *  client's id
     */
    public int getId() {
        return id;
    }

    /**
     *  client's name
     */
    public @NotNull String getName() {
        return name;
    }

    /**
     *  client's surname
     */
    public @NotNull String getSurname() {
        return surname;
    }

    /**
     *  the address of the client's residence
     */
    public String getAddress() {
        return address;
    }

    /**
     *  customer's passport number
     */
    public String getPassportNumber() {
        return passportNumber;
    }

    /**
     *  id of the {@link Bank} that the client belongs to
     */
    public int getBankId() {
        return bankId;
    }

    /**
     * Set client's id (only for this object)
     * @param id new value
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set the address of the client's residence (only for this object)
     * @param address new value
     */
    public void setAddress(String address) {
        this.address = (address == null || address.trim().isEmpty()) ? null : address;
    }

    /**
     * Set customer's passport number (only for this object)
     * @param passportNumber new value
     */
    public void setPassportNumber(String passportNumber) {
        this.passportNumber = (address == null || passportNumber.trim().isEmpty()) ? null : passportNumber;
    }
}