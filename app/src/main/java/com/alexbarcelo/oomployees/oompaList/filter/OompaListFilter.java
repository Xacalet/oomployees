package com.alexbarcelo.oomployees.oompaList.filter;

import com.alexbarcelo.commons.di.ActivityScoped;
import com.alexbarcelo.oomployees.data.model.Oompa;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@ActivityScoped
public class OompaListFilter {

    private String mGender = "";
    private String mProfession = "";

    @Inject
    public OompaListFilter() {
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public String getProfession() {
        return mProfession;
    }

    public void setProfession(String profession) {
        mProfession = profession;
    }

    public List<Oompa> filter(List<Oompa> list) {
        List<Oompa> filteredList = new ArrayList<>();
        for(Oompa item : list) {
            if ((mProfession.isEmpty() || item.profession().equalsIgnoreCase(mProfession)) &&
                (mGender.isEmpty() || item.gender().equalsIgnoreCase(mGender))) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }
}
