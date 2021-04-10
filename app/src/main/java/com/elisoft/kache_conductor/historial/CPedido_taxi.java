package com.elisoft.kache_conductor.historial;

/**
 * Created by elisoft on 06-03-17.
 */


public class CPedido_taxi {
    private String id;
    private int id_usuario;

    private int estado_pedido;
    private String fecha_pedido;
    private String nombre;
    private String apellido;
    private String celular;
    private String marca;
    private String placa;
    private String indicacion;
    private String descripcion;
    private String monto_total;
    private double latitud;
    private double longitud;
    private int clase_vehiculo;
    private int calificacion_vehiculo;
    private int calificacion_conductor;

    public CPedido_taxi()
    {

    }
    public CPedido_taxi(String  id,
                        int id_usuario,
                        int estado_pedido,
                        String fecha_pedido,
                        String nombre,
                        String apellido,
                        String celular,
                        String indicacion,
                        String descripcion,
                        double latitud,
                        double longitud,
                        String monto_total,
                        int clase_vehiculo,
                        int calificacion_conductor,
                        int calificacion_vehiculo)
    {

        this.id= id;
        this.id_usuario=id_usuario;
        this.estado_pedido=estado_pedido;
        this.fecha_pedido=fecha_pedido;
        this.nombre=nombre;
        this.apellido=apellido;
        this.celular=celular;
        this.indicacion=indicacion;
        this.setDescripcion(descripcion);
        this.latitud=latitud;
        this.longitud=longitud;
        this.monto_total=monto_total;
        this.monto_total=monto_total;
        this.setClase_vehiculo(clase_vehiculo);
        this.setCalificacion_conductor(calificacion_conductor);
        this.setCalificacion_vehiculo(calificacion_vehiculo);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getEstado_pedido() {
        return estado_pedido;
    }

    public void setEstado_pedido(int estado_pedido) {
        this.estado_pedido = estado_pedido;
    }

    public String getFecha_pedido() {
        return fecha_pedido;
    }

    public void setFecha_pedido(String fecha_pedido) {
        this.fecha_pedido = fecha_pedido;
    }

    public String getNombre() {
        return nombre;
    }
    public String getMonto_total() {
        return monto_total;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getIndicacion() {
        return indicacion;
    }

    public void setIndicacion(String indicacion) {
        this.indicacion = indicacion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setMonto_total(String monto_total) {
        this.monto_total= monto_total;
    }

    @Override
    public String toString() {
        return "Cpedido_taxi{" +
                "id='" + id + '\'' +
                ",id_usuario='" + id_usuario + '\'' +
                ", estado_pedido='" + estado_pedido + '\'' +
                ", fecha_pedido='" + fecha_pedido + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", celular='" + celular + '\'' +
                ", indicacion='" + indicacion + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", latitud='" + latitud + '\'' +
                ", longitud='" + longitud + '\'' +
                ", monto_total='" + monto_total + '\'' +
                '}';
    }

    public int getClase_vehiculo() {
        return clase_vehiculo;
    }

    public void setClase_vehiculo(int clase_vehiculo) {
        this.clase_vehiculo = clase_vehiculo;
    }

    public int getCalificacion_vehiculo() {
        return calificacion_vehiculo;
    }

    public void setCalificacion_vehiculo(int calificacion_vehiculo) {
        this.calificacion_vehiculo = calificacion_vehiculo;
    }

    public int getCalificacion_conductor() {
        return calificacion_conductor;
    }

    public void setCalificacion_conductor(int calificacion_conductor) {
        this.calificacion_conductor = calificacion_conductor;
    }

}
