        package mobdev.agrikita.pages.addons;

        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.DialogFragment;

        import java.io.Serializable;

        import mobdev.agrikita.models.order.Orders;
        import mobdev.agrikita.R;

        public class BuyAgainDialog extends DialogFragment {

            private static final String ARG_ORDER = "order";
            private Orders order;

            public static BuyAgainDialog newInstance(Orders order) {
                BuyAgainDialog dialog = new BuyAgainDialog();
                Bundle args = new Bundle();
                args.putSerializable(ARG_ORDER, (Serializable) order); // Order must implement Serializable
                dialog.setArguments(args);
                return dialog;
            }

            @Override
            public void onStart() {
                super.onStart();
                if (getDialog() != null && getDialog().getWindow() != null) {
                    getDialog().getWindow().setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                }
            }



            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                if (getArguments() != null) {
                    order = (Orders) getArguments().getSerializable(ARG_ORDER);
                }
               // setStyle(DialogFragment.STYLE_NORMAL, R.style.CenteredDialog); // Apply custom style
            }

            @Nullable
            @Override
            public View onCreateView(@NonNull LayoutInflater inflater,
                                     @Nullable ViewGroup container,
                                     @Nullable Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.dialog_buy_again, container, false);
               // View view = inflater.inflate(R.layout.bottom_sheet_order_details, container, false);
                // Bind views

                LinearLayout itemsContainer = view.findViewById(R.id.itemsContainer);
                TextView tvTotal      = view.findViewById(R.id.tvTotal);

                Button addToCartButton   = view.findViewById(R.id.btnAddToCart);
                Button cancelButton      = view.findViewById(R.id.btnCancel);

                // Populate header fields

                tvTotal.setText("₱" + order.getTotal());

                // Display the full itemDetails string in a new TextView inside the container
                TextView tvDetails = new TextView(requireContext());
                tvDetails.setText(order.getItemDetails());
                tvDetails.setTextSize(16f);
                // optional: set margins
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                lp.topMargin = (int) (8 * getResources().getDisplayMetrics().density);
                tvDetails.setLayoutParams(lp);

                itemsContainer.addView(tvDetails);

                addToCartButton.setOnClickListener(v -> {
                    Toast.makeText(getContext(), "Added to cart!", Toast.LENGTH_SHORT).show();
                    dismiss();
                });
                cancelButton.setOnClickListener(v -> dismiss());

                return view;
            }

        }
