/*
 * Copyright 2014 Evgeniy Khist.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.urlshortener.web;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.urlshortener.service.UrlShortenerService;

/**
 *
 * @author Evgeniy Khist
 */
@Path("/")
public class UrlShortener {
    
    @Inject
    private UrlShortenerService urlShortener;
    
    @POST
    @Path("/")
    public Response shortenUrl(@Context HttpServletRequest request, String url) throws MalformedURLException {
        String shortendUri = urlShortener.shortenUrl(url);
        URL baseUrl = new URL(request.getRequestURL().toString());
        String shortendUrl = new URL(baseUrl, shortendUri).toString();
        return Response.ok(shortendUrl).build();
    }
    
    @GET
    @Path("/{shortenedUrl}")
    public Response resolveShortenedUrl(@PathParam("shortenedUrl") String shortenedUrl) throws URISyntaxException {
        String resolvedUrl = urlShortener.resolveShortenedUrl(shortenedUrl);
        if (resolvedUrl != null) {
            return Response.seeOther(new URI(resolvedUrl)).entity(resolvedUrl).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}