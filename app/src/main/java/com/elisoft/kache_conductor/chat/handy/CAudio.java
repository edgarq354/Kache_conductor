package com.elisoft.kache_conductor.chat.handy;

public class CAudio {
    public boolean left;
    public String mensaje;
    public String tipo;
    public String canal;
    public String fecha;
    public String hora;
    public String titulo;
    int estado=0;
    int id_usuario=0;
    int id_conductor=0;
    int id_chat=0;
    int id_administrador=0;
    int yo=0;
    int visto=0;

    public CAudio(int id_chat,boolean left, String message,
                  String titulo, String fecha,
                  String hora,
                  int estado,
                  int id_usuario,
                  int id_conductor,
                  int yo,String tipo,
                  String canal,
                  int id_administrador) {
        super();
        this.id_chat=id_chat;
        this.left = left;
        this.mensaje = message;
        this.titulo=titulo;
        this.fecha=fecha;
        this.estado=estado;
        this.id_usuario=id_usuario;
        this.id_conductor=id_conductor;
        this.id_conductor=id_administrador;
        this.hora=hora;
        this.yo=yo;
        this.tipo=tipo;
        this.canal=canal;
    }
}