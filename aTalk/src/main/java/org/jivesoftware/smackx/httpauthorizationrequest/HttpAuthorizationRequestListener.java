/**
 *
 *  Copyright 2019-2023 Eng Chong Meng
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jivesoftware.smackx.httpauthorizationrequest;

import org.jivesoftware.smackx.httpauthorizationrequest.element.ConfirmExtension;
import org.jxmpp.jid.DomainBareJid;

/**
 * Interface for listen on receive of HTTP Request IQ or Message.
 * XEP-0070: Verifying HTTP Requests via XMPP (1.0.1 (2016-12-09))
 */
public interface HttpAuthorizationRequestListener {
    void onHttpAuthorizationRequest(DomainBareJid from, ConfirmExtension confirmExtension, String instruction);
}
