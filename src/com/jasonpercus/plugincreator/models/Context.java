/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, December 2021
 */
package com.jasonpercus.plugincreator.models;



import com.jasonpercus.plugincreator.exceptions.ErrorContextException;



/**
 * Represents the context of an action
 * @author JasonPercus
 * @version 1.0
 */
public class Context implements CharSequence {
    
    
    
//ATTRIBUT
    /**
     * Corresponds to the context of an action
     */
    private final String context;

    
    
//CONSTRUCTOR
    /**
     * Create a context of an action
     * @param context Corresponds to the context of an action
     * @throws ErrorContextException If there is a context error
     * @throws NullPointerException If the context is null
     */
    public Context(String context) throws ErrorContextException, NullPointerException {
        if(context == null)   throw new NullPointerException("context is null !");
        if(context.isEmpty()) throw new ErrorContextException("context is empty !");
        this.context = context;
    }
    

    
//METHODES PUBLICS
    /**
     * Returns the length of the context
     * @return Returns the length of the context
     */
    @Override
    public int length() {
        return context.length();
    }

    /**
     * Returns a character from the context
     * @param index Corresponds to the index of the character to retrieve
     * @return Returns a character from the context
     */
    @Override
    public char charAt(int index) {
        return this.context.charAt(index);
    }

    /**
     * Returns a substring of the context
     * @param start Corresponds to the starting index of the capture of the substring
     * @param end Corresponds to the end index of the capture of the substring
     * @return Returns a substring of the context
     */
    @Override
    public CharSequence subSequence(int start, int end) {
        return this.context.subSequence(start, end);
    }

    /**
     * Return the context hashcode
     * @return Return the context hashcode
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + java.util.Objects.hashCode(this.context);
        return hash;
    }

    /**
     * Determines if two contexts are the same or not
     * @param obj Corresponds to the second context to compare to the current
     * @return Returns true if they are identical, otherwise false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Context other = (Context) obj;
        return java.util.Objects.equals(this.context, other.context);
    }

    /**
     * Return the context as a string
     * @return Return the context as a string
     */
    @Override
    public String toString() {
        return context;
    }
    
    
    
}