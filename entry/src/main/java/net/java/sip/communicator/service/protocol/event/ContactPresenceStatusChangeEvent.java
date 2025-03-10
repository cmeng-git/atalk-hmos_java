/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package net.java.sip.communicator.service.protocol.event;

import androidx.annotation.NonNull;

import java.beans.PropertyChangeEvent;

import net.java.sip.communicator.service.protocol.Contact;
import net.java.sip.communicator.service.protocol.ContactGroup;
import net.java.sip.communicator.service.protocol.PresenceStatus;
import net.java.sip.communicator.service.protocol.ProtocolProviderService;

import org.jxmpp.jid.Jid;

/**
 * Instances of this class represent a change in the status of a particular contact.
 *
 * @author Emil Ivov
 * @author Eng Chong Meng
 */
public class ContactPresenceStatusChangeEvent extends PropertyChangeEvent {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 0L;

    /**
     * The contact's FullJid that trigger the event.
     */
    private final Jid entityJid;

    /**
     * The contact's <code>ProtocolProviderService</code>.
     */
    private final ProtocolProviderService sourceProvider;

    /**
     * The parent group of the contact.
     */
    private final ContactGroup parentGroup;

    /**
     * When not the status but just the resource of the contact has changed, for those protocols
     * that support resources.
     */
    private final boolean resourceChanged;

    private final boolean capsExtension;


    /**
     * Creates an event instance indicating that the specified source contact has changed status
     * from <code>oldValue</code> to <code>newValue</code>.
     *
     * @param source the provider that generated the event
     * @param jid the FullJid (BareJid from chatRoom) that generated the event
     * @param sourceProvider the protocol provider that the contact belongs to.
     * @param parentGroup the group containing the contact that caused this event (to be set as null in cases
     * where groups are not supported);
     * @param oldValue the status the source contact was in before entering the new state.
     * @param newValue the status the source contact is currently in.
     * @param capsExtension true if presence stanza contains capExtension.
     */
    public ContactPresenceStatusChangeEvent(Contact source, Jid jid, ProtocolProviderService sourceProvider,
            ContactGroup parentGroup, PresenceStatus oldValue, PresenceStatus newValue, boolean resourceChanged,
            boolean capsExtension) {
        super(source, ContactPresenceStatusChangeEvent.class.getName(), oldValue, newValue);
        this.entityJid = jid;
        this.sourceProvider = sourceProvider;
        this.parentGroup = parentGroup;
        this.resourceChanged = resourceChanged;
        this.capsExtension = capsExtension;
    }

    /**
     * Returns the provider that the source contact belongs to.
     *
     * @return the provider that the source contact belongs to.
     */
    public ProtocolProviderService getSourceProvider() {
        return sourceProvider;
    }

    /**
     * Returns the provider that the source contact belongs to.
     *
     * @return the provider that the source contact belongs to.
     */
    public Contact getSourceContact() {
        return (Contact) getSource();
    }

    /**
     * Returns the FullJid that the source contact belongs to.
     *
     * @return the FullJid that the source contact belongs to.
     */
    public Jid getJid() {
        return entityJid;
    }

    /**
     * Returns the status of the provider before this event took place.
     *
     * @return a PresenceStatus instance indicating the event the source provider was in before it
     * entered its new state.
     */
    public PresenceStatus getOldStatus() {
        return (PresenceStatus) super.getOldValue();
    }

    /**
     * Returns the status of the provider after this event took place. (i.e. at the time the event
     * is being dispatched).
     *
     * @return a PresenceStatus instance indicating the event the source provider is in after the
     * status change occurred.
     */
    public PresenceStatus getNewStatus() {
        return (PresenceStatus) super.getNewValue();
    }

    /**
     * Returns (if applicable) the group containing the contact that cause this event. In the case
     * of a non persistent presence operation set this field is null.
     *
     * @return the ContactGroup (if there is one) containing the contact that caused the event.
     */
    public ContactGroup getParentGroup() {
        return parentGroup;
    }

    /**
     * When the event fired is change in the resource of the contact will return <code>true</code>.
     *
     * @return the event fired is only for change in the resource of the contact.
     */
    public boolean isResourceChanged() {
        return resourceChanged;
    }

    public boolean hasCapsExtension() {
        return capsExtension;
    }

    /**
     * Returns a String representation of this ContactPresenceStatusChangeEvent
     *
     * @return A a String representation of this ContactPresenceStatusChangeEvent.
     */
    @NonNull
    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder("ContactPresenceStatusChangeEvent-[ ContactID=");
        buff.append(getSourceContact().getAddress());
        if (getParentGroup() != null)
            buff.append(", ParentGroup").append(getParentGroup().getGroupName());
        buff.append(", OldStatus=").append(getOldStatus())
                .append(", NewStatus=").append(getNewStatus())
                .append("]");
        return buff.toString();
    }
}
