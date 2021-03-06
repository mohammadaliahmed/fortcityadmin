package com.appsinventiv.toolsbazzaradmin.Models;

/**
 * Created by AliAh on 17/09/2018.
 */

public class TermsModel {
    String terms, cookies, license, hyperlink, iframes, contentLiability, reservation, removal, disclaimer, replacement, other;

    public TermsModel(String terms, String cookies, String license, String hyperlink, String iframes, String contentLiability, String reservation, String removal, String disclaimer, String replacement, String other) {
        this.terms = terms;
        this.cookies = cookies;
        this.license = license;
        this.hyperlink = hyperlink;
        this.iframes = iframes;
        this.contentLiability = contentLiability;
        this.reservation = reservation;
        this.removal = removal;
        this.disclaimer = disclaimer;
        this.replacement = replacement;
        this.other = other;
    }

    public TermsModel() {
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getHyperlink() {
        return hyperlink;
    }

    public void setHyperlink(String hyperlink) {
        this.hyperlink = hyperlink;
    }

    public String getIframes() {
        return iframes;
    }

    public void setIframes(String iframes) {
        this.iframes = iframes;
    }

    public String getContentLiability() {
        return contentLiability;
    }

    public void setContentLiability(String contentLiability) {
        this.contentLiability = contentLiability;
    }

    public String getReservation() {
        return reservation;
    }

    public void setReservation(String reservation) {
        this.reservation = reservation;
    }

    public String getRemoval() {
        return removal;
    }

    public void setRemoval(String removal) {
        this.removal = removal;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
