/*
 * aTalk, android VoIP and Instant Messaging client
 * Copyright 2014~2024 Eng Chong Meng
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jivesoftware.smackx.thumbnail.provider;

import java.io.IOException;

import org.jivesoftware.smack.packet.XmlEnvironment;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.jivesoftware.smack.xml.XmlPullParser;
import org.jivesoftware.smack.xml.XmlPullParserException;
import org.jivesoftware.smackx.thumbnail.element.Thumbnail;

public class ThumbnailProvider extends ExtensionElementProvider<Thumbnail> {

    public static final ThumbnailProvider INSTANCE = new ThumbnailProvider();

    @Override
    public Thumbnail parse(XmlPullParser parser, int initialDepth, XmlEnvironment xmlEnvironment) throws XmlPullParserException, IOException {
        String uri = parser.getAttributeValue(null, Thumbnail.URI);
        String mediaType = parser.getAttributeValue(null, Thumbnail.MEDIA_TYPE);
        int width = Integer.parseInt(parser.getAttributeValue(null, Thumbnail.WIDTH));
        int height = Integer.parseInt(parser.getAttributeValue(null, Thumbnail.HEIGHT));
        return new Thumbnail(uri, mediaType, width, height);
    }
}
