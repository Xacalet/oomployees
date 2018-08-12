package com.alexbarcelo.oomployees.data.source;

import com.alexbarcelo.oomployees.data.model.Favorite;
import com.alexbarcelo.oomployees.data.model.Oompa;
import com.alexbarcelo.oomployees.data.model.PaginatedOompaList;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class OompaMockRepository implements OompaRepository {

    private final int PAGE_COUNT = 20;

    @Override
    public Single<PaginatedOompaList> getOompas(int page) {
        List<Oompa> oompaList = new ArrayList<>();
        int PAGE_SIZE = 25;
        for (int i = 0; i < PAGE_SIZE; i++){
            Oompa oompa = createOompa(getId(page, i));
            oompaList.add(oompa);
        }
        return Single.just(PaginatedOompaList.create(page, PAGE_COUNT, oompaList));
    }

    @Override
    public Single<Oompa> getOompa(long id) {
        return Single.just(createOompa(id));
    }

    private long getId(int pageNum, int posInPage) {
        return (posInPage + 1) + ((pageNum - 1) * PAGE_COUNT);
    }

    private Oompa createOompa(long id) {
        Favorite favorite = Favorite.create("red","chocolat","", "");
        return Oompa.create("Karadzas", "Marcy", favorite, "F",
                "https://s3.eu-central-1.amazonaws.com/napptilus/level-test/1.jpg",
                "Developer", "mkaradzas1@visualengin.com", 21, "Loompalandia",
                99, id, null,  "Lorem ipsum dolor sit amet, consectetur adipiscing elit");

    }
}
