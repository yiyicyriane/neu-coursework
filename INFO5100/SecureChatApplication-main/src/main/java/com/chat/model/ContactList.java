package com.chat.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor                // For frameworks like Spring to create empty objects
// @RequiredArgsConstructor         // constructor with NonNull args
@AllArgsConstructor               // Optional if you still want a constructor with all args
public class ContactList {
    private List<MembersInContactList> contactList = new ArrayList<>(); //stores a list of members in contact list objects.

    /**
     * Returns a copy of the members of connect list to prevent modification of the original list.
     * @return A new List containing MembersInContactList objects.
     */
    public List<MembersInContactList> getContactList() {
        return new ArrayList<>(contactList);
    }

    /**
     * Sets the entire contact list.
     * @param contactList The list to set.
     */
    public void setContactList(List<MembersInContactList> contactList) {
        if (contactList != null) {
            this.contactList = new ArrayList<>(contactList); // Avoids direct reference, ensuring immutability.
        }
    }

    /**
     * Adds a new member to the contact list.
     * @param member The member to be added.
     */
    public void addContact(MembersInContactList member) {
        if (member != null && !contactList.contains(member)) {
            contactList.add(member);
        }
    }

    /**
     * Removes a member from the contact list by their ID.
     * @param memberId The ID of the member to remove.
     * @return true if the member was removed successfully, false otherwise.
     */
    public boolean removeContact(String memberId) {
        Optional<MembersInContactList> memberToRemove = contactList.stream()
                .filter(member -> member.getUserId().equals(memberId))
                .findFirst();

        if (memberToRemove.isPresent()) {
            contactList.remove(memberToRemove.get());
            return true;
        }

        return false;
    }

}
