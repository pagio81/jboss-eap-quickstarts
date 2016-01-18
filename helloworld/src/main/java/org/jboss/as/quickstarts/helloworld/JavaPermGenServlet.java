/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.helloworld;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * A simple servlet taking advantage of features added in 3.0.
 * </p>
 *
 * <p>
 * The servlet is registered and mapped to /HelloServlet using the {@linkplain WebServlet
 * @HttpServlet}. The {@link HelloService} is injected by CDI.
 * </p>
 *
 * @author Pete Muir
 *
 */
@SuppressWarnings("serial")
@WebServlet("/JavaPermGen")
public class JavaPermGenServlet extends HttpServlet {

    static String PAGE_HEADER = "<html><head><title>Java PermGen</title></head><body>";

    static String PAGE_FOOTER = "</body></html>";

    @Inject
    HelloService helloService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        writer.println(PAGE_HEADER);
        writer.println("<h1>Starting to loop</h1>");

        int loopCount = req.getParameter("loopCount")==null?Integer.MAX_VALUE:Integer.parseInt(req.getParameter("loopCount"));

        for(int i=0; i< loopCount; i++){

            String className = "org.jboss.as.quickstarts.helloworld.HellobEAN"+i;

            final Map<String, Class<?>> properties = new HashMap<String, Class<?>>();
            properties.put("foo", Integer.class);
            properties.put("bar", String.class);
            properties.put("baz", int[].class);

            final Class<?> beanClass =
                    createBeanClass(className, properties);

            writer.println("<p>Adding in memory class:"+i+"</p>");
            if(i % 1000 == 0)
                writer.flush();
        }

        writer.println(PAGE_FOOTER);
        writer.close();
    }


    public static Class<?> createBeanClass(
        /* fully qualified class name */
        final String className,
        /* bean properties, name -> type */
        final Map<String, Class<?>> properties){

            final BeanGenerator beanGenerator = new BeanGenerator();

        /* use our own hard coded class name instead of a real naming policy */
            beanGenerator.setNamingPolicy(new NamingPolicy(){

                @Override public String getClassName(final String prefix,
                                                     final String source, final Object key, final Predicate names){
                    return className;
                }});
            BeanGenerator.addProperties(beanGenerator, properties);
            return (Class<?>) beanGenerator.createClass();
    }



}
