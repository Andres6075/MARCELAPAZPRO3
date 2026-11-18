package com.marcelapaz.microservicio.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "modulos_aprendizaje")
public class ModuloEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titulo;
    @Column(length = 1000)
    private String descripcion;
    private String nivelEducativo;
    private String asignatura;
    private String unidad;
    private String objetivoAprendizaje;
    private String codigoOficial;
    private String ejeCurricular;
    @Column(length = 1000)
    private String explicacion;
    private Boolean tieneAudioApoyo;
    private Integer intentosPermitidos;
    private Integer intentosRealizados;
    private String dificultad;
    private Float progreso;
    private Boolean estaCompletado;
    private Integer horasEstimadas;
    private Boolean aprobadoPorUtp;

    public ModuloEntity() {}

    public ModuloEntity(Integer id, String titulo, String descripcion, String nivelEducativo, String asignatura, String unidad, String objetivoAprendizaje, String codigoOficial, String ejeCurricular, String explicacion, Boolean tieneAudioApoyo, Integer intentosPermitidos, Integer intentosRealizados, String dificultad, Float progreso, Boolean estaCompletado, Integer horasEstimadas, Boolean aprobadoPorUtp) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.nivelEducativo = nivelEducativo;
        this.asignatura = asignatura;
        this.unidad = unidad;
        this.objetivoAprendizaje = objetivoAprendizaje;
        this.codigoOficial = codigoOficial;
        this.ejeCurricular = ejeCurricular;
        this.explicacion = explicacion;
        this.tieneAudioApoyo = tieneAudioApoyo;
        this.intentosPermitidos = intentosPermitidos;
        this.intentosRealizados = intentosRealizados;
        this.dificultad = dificultad;
        this.progreso = progreso;
        this.estaCompletado = estaCompletado;
        this.horasEstimadas = horasEstimadas;
        this.aprobadoPorUtp = aprobadoPorUtp;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getNivelEducativo() { return nivelEducativo; }
    public void setNivelEducativo(String nivelEducativo) { this.nivelEducativo = nivelEducativo; }
    public String getAsignatura() { return asignatura; }
    public void setAsignatura(String asignatura) { this.asignatura = asignatura; }
    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }
    public String getObjetivoAprendizaje() { return objetivoAprendizaje; }
    public void setObjetivoAprendizaje(String objetivoAprendizaje) { this.objetivoAprendizaje = objetivoAprendizaje; }
    public String getCodigoOficial() { return codigoOficial; }
    public void setCodigoOficial(String codigoOficial) { this.codigoOficial = codigoOficial; }
    public String getEjeCurricular() { return ejeCurricular; }
    public void setEjeCurricular(String ejeCurricular) { this.ejeCurricular = ejeCurricular; }
    public String getExplicacion() { return explicacion; }
    public void setExplicacion(String explicacion) { this.explicacion = explicacion; }
    public Boolean getTieneAudioApoyo() { return tieneAudioApoyo; }
    public void setTieneAudioApoyo(Boolean tieneAudioApoyo) { this.tieneAudioApoyo = tieneAudioApoyo; }
    public Integer getIntentosPermitidos() { return intentosPermitidos; }
    public void setIntentosPermitidos(Integer intentosPermitidos) { this.intentosPermitidos = intentosPermitidos; }
    public Integer getIntentosRealizados() { return intentosRealizados; }
    public void setIntentosRealizados(Integer intentosRealizados) { this.intentosRealizados = intentosRealizados; }
    public String getDificultad() { return dificultad; }
    public void setDificultad(String dificultad) { this.dificultad = dificultad; }
    public Float getProgreso() { return progreso; }
    public void setProgreso(Float progreso) { this.progreso = progreso; }
    public Boolean getEstaCompletado() { return estaCompletado; }
    public void setEstaCompletado(Boolean estaCompletado) { this.estaCompletado = estaCompletado; }
    public Integer getHorasEstimadas() { return horasEstimadas; }
    public void setHorasEstimadas(Integer horasEstimadas) { this.horasEstimadas = horasEstimadas; }
    public Boolean getAprobadoPorUtp() { return aprobadoPorUtp; }
    public void setAprobadoPorUtp(Boolean aprobadoPorUtp) { this.aprobadoPorUtp = aprobadoPorUtp; }
}
