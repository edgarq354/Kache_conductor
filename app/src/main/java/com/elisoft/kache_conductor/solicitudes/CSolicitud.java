package com.elisoft.kache_conductor.solicitudes;

public class CSolicitud {
    private String id;
    private String latitud;
    private String longitud;
    private String latitud_inicio;
    private String longitud_inicio;
    private String latitud_final;
    private String longitud_final;
    private String direccion;
    private String direccion_inicio;
    private String direccion_final;
    private String monto_total;
    private String id_vehiculo;
    private String id_conductor;
    private String id_usuario;
    private String nombre_usuario;
    private String nombre_conductor;
    private String numero_movil;
    private String direccion_imagen_usuario;
    private String fecha_pedido;
    private String fecha_proceso;
    private String calificacion_conductor;
    private String calificacion_vehiculo;
    private String estado;
    private String clase_vehiculo;
    private String tiempo;
    private String distancia;
    private String monto_pedido;

    public CSolicitud() {
    }

    public CSolicitud(String id,
                      String latitud,
                      String longitud,
                      String latitud_inicio,
                      String longitud_inicio,
                      String latitud_final,
                      String longitud_final,
                      String direccion,
                      String direccion_inicio,
                      String direccion_final,
                      String monto_total,
                      String id_vehiculo,
                      String id_conductor,
                      String id_usuario,
                      String nombre_usuario,
                      String nombre_conductor,
                      String numero_movil,
                      String direccion_imagen_usuario,
                      String fecha_pedido,
                      String fecha_proceso,
                      String calificacion_conductor,
                      String calificacion_vehiculo,
                      String estado,
                      String clase_vehiculo,
                      String tiempo,
                      String distancia,
                      String monto_pedido
    ) {
        this.id = id;
        this.latitud = latitud;
        this.longitud = longitud;
        this.latitud_inicio = latitud_inicio;
        this.longitud_inicio = longitud_inicio;
        this.latitud_final = latitud_final;
        this.longitud_final = longitud_final;
        this.direccion = direccion;
        this.direccion_inicio = direccion_inicio;
        this.direccion_final = direccion_final;
        this.monto_total = monto_total;
        this.id_vehiculo = id_vehiculo;
        this.id_conductor = id_conductor;
        this.id_usuario = id_usuario;
        this.nombre_usuario = nombre_usuario;
        this.nombre_conductor = nombre_conductor;
        this.numero_movil = numero_movil;
        this.direccion_imagen_usuario = direccion_imagen_usuario;
        this.fecha_pedido = fecha_pedido;
        this.fecha_proceso = fecha_proceso;
        this.calificacion_conductor = calificacion_conductor;
        this.calificacion_vehiculo = calificacion_vehiculo;
        this.estado = estado;
        this.setClase_vehiculo(clase_vehiculo);
        this.setTiempo(tiempo);
        this.setDistancia(distancia);
        this.setMonto_pedido(monto_pedido);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud_inicio() {
        return latitud_inicio;
    }

    public void setLatitud_inicio(String latitud_inicio) {
        this.latitud_inicio = latitud_inicio;
    }

    public String getLongitud_inicio() {
        return longitud_inicio;
    }

    public void setLongitud_inicio(String longitud_inicio) {
        this.longitud_inicio = longitud_inicio;
    }

    public String getLatitud_final() {
        return latitud_final;
    }

    public void setLatitud_final(String latitud_final) {
        this.latitud_final = latitud_final;
    }

    public String getLongitud_final() {
        return longitud_final;
    }

    public void setLongitud_final(String longitud_final) {
        this.longitud_final = longitud_final;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDireccion_inicio() {
        return direccion_inicio;
    }

    public void setDireccion_inicio(String direccion_inicio) {
        this.direccion_inicio = direccion_inicio;
    }

    public String getDireccion_final() {
        return direccion_final;
    }

    public void setDireccion_final(String direccion_final) {
        this.direccion_final = direccion_final;
    }

    public String getMonto_total() {
        return monto_total;
    }

    public void setMonto_total(String monto_total) {
        this.monto_total = monto_total;
    }

    public String getId_vehiculo() {
        return id_vehiculo;
    }

    public void setId_vehiculo(String id_vehiculo) {
        this.id_vehiculo = id_vehiculo;
    }

    public String getId_conductor() {
        return id_conductor;
    }

    public void setId_conductor(String id_conductor) {
        this.id_conductor = id_conductor;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getNombre_conductor() {
        return nombre_conductor;
    }

    public void setNombre_conductor(String nombre_conductor) {
        this.nombre_conductor = nombre_conductor;
    }

    public String getNumero_movil() {
        return numero_movil;
    }

    public void setNumero_movil(String numero_movil) {
        this.numero_movil = numero_movil;
    }

    public String getDireccion_imagen_usuario() {
        return direccion_imagen_usuario;
    }

    public void setDireccion_imagen_usuario(String direccion_imagen_usuario) {
        this.direccion_imagen_usuario = direccion_imagen_usuario;
    }

    public String getFecha_pedido() {
        return fecha_pedido;
    }

    public void setFecha_pedido(String fecha_pedido) {
        this.fecha_pedido = fecha_pedido;
    }

    public String getFecha_proceso() {
        return fecha_proceso;
    }

    public void setFecha_proceso(String fecha_proceso) {
        this.fecha_proceso = fecha_proceso;
    }

    public String getCalificacion_conductor() {
        return calificacion_conductor;
    }

    public void setCalificacion_conductor(String calificacion_conductor) {
        this.calificacion_conductor = calificacion_conductor;
    }

    public String getCalificacion_vehiculo() {
        return calificacion_vehiculo;
    }

    public void setCalificacion_vehiculo(String calificacion_vehiculo) {
        this.calificacion_vehiculo = calificacion_vehiculo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getClase_vehiculo() {
        return clase_vehiculo;
    }

    public void setClase_vehiculo(String clase_vehiculo) {
        this.clase_vehiculo = clase_vehiculo;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public String getMonto_pedido() {
        return monto_pedido;
    }

    public void setMonto_pedido(String monto_pedido) {
        this.monto_pedido = monto_pedido;
    }
}
