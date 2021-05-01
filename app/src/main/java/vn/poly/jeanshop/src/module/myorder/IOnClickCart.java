package vn.poly.jeanshop.src.module.myorder;

import android.widget.TextView;

import vn.poly.jeanshop.src.model.OrderProvisional;

public interface IOnClickCart {
    void increase(OrderProvisional orderProvisional, TextView numberCart);
    void reduce(OrderProvisional orderProvisional, TextView numberCart);
}
