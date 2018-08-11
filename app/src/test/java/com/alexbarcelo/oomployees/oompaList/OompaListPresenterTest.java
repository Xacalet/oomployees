package com.alexbarcelo.oomployees.oompaList;

import com.alexbarcelo.oomployees.data.model.PaginatedOompaList;
import com.alexbarcelo.oomployees.data.source.OompaRepository;
import com.alexbarcelo.oomployees.oompaList.filter.OompaListFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.when;

/**
 * Batería de test para el presentador de la lista de Oompa-Loompa
 */
public class OompaListPresenterTest {

    @Mock
    private OompaRepository mOompaRepository;

    @Mock
    private OompaListContract.View mOompaListView;

    private OompaListPresenter mOompaListPresenter;

    @Before
    public void setupTasksPresenter() {
        // Inicializamos los objetos mockeados
        MockitoAnnotations.initMocks(this);

        mOompaListPresenter = new OompaListPresenter(mOompaRepository, Schedulers.trampoline(), Schedulers.trampoline(), new OompaListFilter());
        mOompaListPresenter.takeView(mOompaListView);

        // Como no hay fragmeno detrás de la vista, indicamos que la vista ya está activa.
        when(mOompaListView.isActive()).thenReturn(true);
    }

    /**
     * La llamada al método loadMoreItems del presenter debería, en caso de éxito:
     *  1. Mostrar spinner de carga de items;
     *  2. Cargar items en la lista;
     *  3. Esconder spinner
     */
    @Test
    public void loadOompasWithSuccessAndLoadIntoView() {
        int currentPage = 1;

        // Mockeamos la respuesta a la llamada getOompas del repositorio
        PaginatedOompaList paginatedList = getMockedOompaList();
        when(mOompaRepository.getOompas(currentPage)).thenReturn(Single.just(paginatedList));

        // Llamamos al método de carga de ítems del presenter
        mOompaListPresenter.loadMoreItems(false);

        // Comprobamos las llamadas a los métodos de la vista
        InOrder inOrder = Mockito.inOrder(mOompaListView);
        inOrder.verify(mOompaListView).setLoadingIndicator(true);
        inOrder.verify(mOompaListView).loadItems(paginatedList.results());
        inOrder.verify(mOompaListView).setLoadingIndicator(false);
    }

    /**
     * La llamada al método loadMoreItems del presenter debería, en caso de error:
     *  1. Mostrar spinner de carga de items;
     *  2. Esconder spinner;
     *  3. Mostrar mensaje de error;
     *  4. Mostrar botón de reintento
     */
    @Test
    public void loadWithErrorDisplaysErrorMessage() {
        int currentPage = 1;
        String error_message = "ERROR_MESSAGE";

        // Mockeamos un error a la llamada getOompas del repositorio
        PaginatedOompaList paginatedList = getMockedOompaList();
        when(mOompaRepository.getOompas(currentPage)).thenReturn(Single.<PaginatedOompaList>error(new Exception(error_message)));

        // Llamamos al método de carga de ítems del presenter
        mOompaListPresenter.loadMoreItems(false);

        // Comprobamos las llamadas a los métodos de la vista
        InOrder inOrder = Mockito.inOrder(mOompaListView);
        inOrder.verify(mOompaListView).setLoadingIndicator(true);
        inOrder.verify(mOompaListView).setLoadingIndicator(false);
        inOrder.verify(mOompaListView).showErrorMessage(error_message);
        inOrder.verify(mOompaListView).setRetryButton(true);
    }

    private PaginatedOompaList getMockedOompaList() {
        return Mockito.mock(PaginatedOompaList.class);
    }
}
