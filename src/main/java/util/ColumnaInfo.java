/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author scastelar
 */
public class ColumnaInfo {
     public String nombre;
        public String tipo;
        public String nulo;
        public String clave;
        public String defecto;

        @Override
        public String toString() {
            return nombre + " (" + tipo + ")";
        }
}
