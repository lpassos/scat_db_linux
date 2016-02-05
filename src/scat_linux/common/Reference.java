/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.common;

/**
 *
 * @author leonardo
 */
public class Reference {
    
    private long line ;
    private String referredFeature ;
    private String context ;

    public Reference(long line, String referredFeature, String context) {
        this.line = line;
        this.referredFeature = referredFeature;
        this.context = context;
    }

    public String getContext() {
        return context;
    }

    public long getLine() {
        return line;
    }

    public String getReferredFeature() {
        return referredFeature;
    }

    @Override
    public int hashCode() {
        return this.context.hashCode() + this.referredFeature.hashCode() * 
                ((int) this.line) ;
    }        

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof Reference))
            return false ;
        
        if (obj == this)
            return true ;
        
        Reference ref = (Reference) obj ;
        return ref.line == this.line && ref.context.equals(this.context) &&
               ref.referredFeature.equals(this.referredFeature) ;
    }        

    @Override
    public String toString() {
        return "[" + line + ", " + referredFeature + ", " + context + "]" ;
    }          
}
