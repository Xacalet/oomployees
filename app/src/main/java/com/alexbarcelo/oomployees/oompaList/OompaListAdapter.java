package com.alexbarcelo.oomployees.oompaList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alexbarcelo.oomployees.GlideApp;
import com.alexbarcelo.oomployees.R;
import com.alexbarcelo.oomployees.data.model.Oompa;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adaptador para lista de oompa-loompas. Incluye un elemento extra al final de la lista que permite
 * mostrar un spinner, para indicar que se están cargando más elementos, o bien un botón de reintento
 * en caso de que la última solicitud haya generado error. Este elemento final será siempre visible
 * (cuando se alcanza el final de la lista), y en caso que no corresponda mostrar ni el spinner ni
 * el botón, aparecerá vacío.
 */
public class OompaListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Oompa> mOompas = new ArrayList<>();
    private Context mContext;
    private FooterItemType mFooterItemType = FooterItemType.NONE;
    private OnItemClickListener mOnItemClickListener;

    OompaListAdapter(Context context) {
        // Necesitamos el contexto para trabajar con Glider
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // En función del tipo de vista calculado en el método getItemViewType, se instancia una
        // clase u otra de view holder para la lista (-1: Oompa, 0: Vacío, 1: Spinner, 2: Botón reintento)
        if (viewType == FooterItemType.NONE.ViewType) {
            // En el caso del elemento vacío, reutiliza uno de los layouts de android.
            return new EmptyViewHolder(inflater.inflate(android.R.layout.simple_list_item_1, parent, false));
        } else if (viewType == FooterItemType.LOADING_INDICATOR.ViewType) {
            // A pesar de que el layout contiene un spinner, no es necesario hacer referencia a éste
            // para interactuar, así que utilizamos la clase EmptyViewHolder también para este tipo.
            return new EmptyViewHolder(inflater.inflate(R.layout.list_loading_indicator_item, parent, false));
        } else if (viewType == FooterItemType.RETRY_BUTTON.ViewType) {
            return new RetryButtonHolder(inflater.inflate(R.layout.list_retry_button_item, parent, false));
        } else {
            return new OompaViewHolder(inflater.inflate(R.layout.list_oompa_item, parent, false));
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position < getItemCount() - 1) {
            Oompa oompa = mOompas.get(position);
            OompaViewHolder oompaViewHolder = (OompaViewHolder) holder;

            oompaViewHolder.fullNameView.setText(String.format("%s, %s", oompa.lastName(), oompa.firstName()));

            oompaViewHolder.professionAndAgeView.setText(String.format("%s, %d", oompa.profession(), oompa.age()));

            oompaViewHolder.genderView.setText(oompa.gender());

            oompaViewHolder.cardView.setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onOompaClick(oompa.id());
                }
            });

            Drawable drawable = DrawableCompat.wrap(oompaViewHolder.genderView.getBackground()).mutate();
            if (oompa.gender().equals("F")) {
                DrawableCompat.setTint(drawable, mContext.getResources().getColor(R.color.colorFemale));
            } else {
                DrawableCompat.setTint(drawable, mContext.getResources().getColor(R.color.colorMale));
            }

            GlideApp.with(mContext.getApplicationContext())
                    .load(oompa.image())
                    .into(oompaViewHolder.imageView);

        } else if (holder.getItemViewType() == FooterItemType.RETRY_BUTTON.ViewType) {
            RetryButtonHolder retryButtonHolder = (RetryButtonHolder) holder;
            retryButtonHolder.retryButton.setOnClickListener(view -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onRetryButtonClick();
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return mFooterItemType.ViewType;
        } else {
            return -1;
        }
    }

    /**
     * El adaptador mostrará tantos ítems como elementos tenga la lista más uno extra al final, que
     * podrá ser un spinner conforme el presenter está cargando más datos, un botón de reintento
     * en caso de error o bien nada (se optó por añadir un elemento vacio y así mantener siempre
     * un elemento al final de la lista, ya que en caso contrario complicaríamos el cálculo de
     * el número de elementos del adaptador.
     */
    @Override
    public int getItemCount() {
        return mOompas.size() + 1;
    }

    public void setItems(List<Oompa> oompas) {
        mOompas  = new ArrayList<>(oompas);
        notifyDataSetChanged();
    }

    public void showLoadingIndicator(boolean active) {
        mFooterItemType = active ? FooterItemType.LOADING_INDICATOR : FooterItemType.NONE;
        notifyItemChanged(getItemCount() - 1);
    }

    public void showRetryButton(boolean active) {
        mFooterItemType = active ? FooterItemType.RETRY_BUTTON : FooterItemType.NONE;
        notifyItemChanged(getItemCount() - 1);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public boolean isRetryButtonActive() {
        return mFooterItemType.ViewType == FooterItemType.RETRY_BUTTON.ViewType;
    }

    /**
     * Eventos de click para los elementos de la lista
     */
    public interface OnItemClickListener {
        void onOompaClick(long id);

        void onRetryButtonClick();
    }

    /**
     * Indica el tipo del elemento extra de la lista: el indicador de cargando elementos, el botón
     * de reintento o nada.
     */
    private enum FooterItemType {
        NONE, LOADING_INDICATOR, RETRY_BUTTON;

        private int ViewType;

        static {
            NONE.ViewType = 0;
            LOADING_INDICATOR.ViewType = 1;
            RETRY_BUTTON.ViewType = 2;
        }
    }

    /**
     * View holder para los ítems de lista que muestran información de oompa-loompas
     */
    public class OompaViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.oompa_card) RelativeLayout cardView;
        @BindView(R.id.oompa_card_image) ImageView imageView;
        @BindView(R.id.oompa_card_full_name) TextView fullNameView;
        @BindView(R.id.oompa_card_profession_and_age) TextView professionAndAgeView;
        @BindView(R.id.oompa_card_gender) TextView genderView;

        OompaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * View holder del botón de retry
     */
    public class RetryButtonHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.retry_button) Button retryButton;

        RetryButtonHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * View holder para los ítems de la lista que no requieren de referencia a ninguna de sus
     * elemento.
     */
    public class EmptyViewHolder extends RecyclerView.ViewHolder {

        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
