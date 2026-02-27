
DELETE FROM detalle_pedido;
DELETE FROM movimiento_stock;
DELETE FROM promocion_producto;
DELETE FROM pedido;
DELETE FROM promocion;
DELETE FROM users;
DELETE FROM producto;
DELETE FROM categoria;
DELETE FROM proveedores;

ALTER TABLE categoria ALTER COLUMN id RESTART WITH 1;
ALTER TABLE proveedores ALTER COLUMN id RESTART WITH 1;
ALTER TABLE producto ALTER COLUMN id RESTART WITH 1;
ALTER TABLE promocion ALTER COLUMN id RESTART WITH 1;
ALTER TABLE pedido ALTER COLUMN id RESTART WITH 1;

INSERT INTO users (username, email, password) VALUES
    ('admin', 'admin@stock.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa');
-- CATEGORIA
INSERT INTO categoria (nombre) VALUES
                                   ('Maquillaje'),
                                   ('Skincare'),
                                   ('Cuidado Capilar'),
                                   ('Fragancias'),
                                   ('Accesorios de Belleza');

-- PROVEEDORES
INSERT INTO proveedores (nombre) VALUES
                                     ('L''Oréal Argentina S.A.'),
                                     ('Procter & Gamble Argentina'),
                                     ('Cosméticos Internacionales SRL'),
                                     ('Laboratorios Beiersdorf');

-- PRODUCTOS
INSERT INTO producto
(nombre, precio, stock, min_stock, descripcion, activo, nombre_busqueda, linea, marca, tono, tamaño, codigo_barra, categoria_id, fk_proveedor)
VALUES
    ('Base Líquida Infallible 24H', 4500.00, 25, 5, 'Base de alta cobertura con efecto matte de larga duración', true, 'Infallible L''Oréal Base 30ml', 'Infallible', 'L''Oréal', 'Natural Beige', '30ml', '3600523374581', 1, 1),

    ('Máscara de Pestañas Volume Million Lashes', 3200.00, 40, 8, 'Máscara para volumen extremo con cepillo exclusivo', true, 'Volume Million Lashes L''Oréal Máscara 9ml', 'Volume Million Lashes', 'L''Oréal', 'Black', '9ml', '3600523374598', 1, 1),

    ('Labial Color Riche Le Rouge', 2800.00, 35, 6, 'Labial hidratante con color intenso y acabado cremoso', true, 'Color Riche Le Rouge L''Oréal Labial 3.5g', 'Color Riche', 'L''Oréal', 'Rouge Passion', '3.5g', '3600523374604', 1, 1),

    ('Delineador Líquido Superliner', 2100.00, 30, 5, 'Delineador de precisión con punta de fieltro', true, 'Superliner L''Oréal Delineador Negro 1.2ml', 'Superliner', 'L''Oréal', 'Black', '1.2ml', '3600523374611', 1, 1);


INSERT INTO producto
(nombre, precio, stock, min_stock, descripcion, activo, nombre_busqueda, linea, marca, tono, tamaño, codigo_barra, categoria_id, fk_proveedor)
VALUES
    ('Serum Revitalift Filler', 5800.00, 20, 4, 'Suero concentrado con ácido hialurónico para relleno de arrugas', true, 'Revitalift Filler L''Oréal Serum 30ml', 'Revitalift Filler', 'L''Oréal', NULL, '30ml', '3600523374628', 2, 1),

    ('Crema Facial Revitalift Día', 4200.00, 25, 5, 'Crema hidratante anti-edad con FPS 20', true, 'Revitalift Día L''Oréal Crema 50ml', 'Revitalift', 'L''Oréal', NULL, '50ml', '3600523374635', 2, 1),

    ('Tónico Hidratant Garnier', 1800.00, 45, 10, 'Tónico facial con agua de rosas para piel normal', true, 'Hidratant Garnier Tónico 200ml', 'Hidratant', 'Garnier', NULL, '200ml', '3600523374642', 2, 2);


INSERT INTO producto
(nombre, precio, stock, min_stock, descripcion, activo, nombre_busqueda, linea, marca, tono, tamaño, codigo_barra, categoria_id, fk_proveedor)
VALUES
    ('Shampoo Elseve Pure Zinc', 1500.00, 60, 12, 'Shampoo purificante para cuero cabelludo graso', true, 'Pure Zinc Elseve Shampoo 250ml', 'Pure Zinc', 'Elseve', NULL, '250ml', '3600523374659', 3, 1),

    ('Acondicionador Elseve Color protector', 1600.00, 55, 10, 'Acondicionador para proteger el color del cabello teñido', true, 'Color Protector Elseve Acondicionador 200ml', 'Color Protector', 'Elseve', NULL, '200ml', '3600523374666', 3, 1),

    ('Mascarilla Capilar Nutritive', 3500.00, 30, 6, 'Mascarilla nutritiva profunda para cabello seco', true, 'Nutritive Elseve Mascarilla 300ml', 'Nutritive', 'Elseve', NULL, '300ml', '3600523374673', 3, 1);


INSERT INTO producto
(nombre, precio, stock, min_stock, descripcion, activo, nombre_busqueda, linea, marca, tono, tamaño, codigo_barra, categoria_id, fk_proveedor)
VALUES
    ('Perfume Chanel N°5 Eau de Parfum', 25000.00, 8, 2, 'Fragancia icónica femenina con notas florales', true, 'Chanel N°5 Perfume 50ml', 'N°5', 'Chanel', NULL, '50ml', '3145891306215', 4, 3),

    ('Perfume Sauvage Dior', 22000.00, 10, 2, 'Fragancia masculina con notas amaderadas', true, 'Sauvage Dior Perfume 60ml', 'Sauvage', 'Dior', NULL, '60ml', '3348901357204', 4, 3),

    ('Agua de Tocolia Giorgio Armani', 18500.00, 12, 3, 'Fragancia acuática femenina fresca', true, 'Acqua di Giò Armani Agua 100ml', 'Acqua di Giò', 'Giorgio Armani', NULL, '100ml', '3348901367808', 4, 3);


INSERT INTO producto
(nombre, precio, stock, min_stock, descripcion, activo, nombre_busqueda, linea, marca, tono, tamaño, codigo_barra, categoria_id, fk_proveedor)
VALUES
    ('Set de Pinceles Nivea', 3200.00, 25, 5, 'Set completo de 5 pinceles sintéticos', true, 'Nivea Set Pinceles 5unidades', 'Beauty', 'Nivea', NULL, '5 unidades', '4005800344678', 5, 4),

    ('Esponja de Maquillaje Blend', 800.00, 50, 10, 'Esponja blendeable para base de maquillaje', true, 'Blend Esponja Maquillaje Universal', 'Blend', 'Generic', NULL, 'Universal', '7798154712345', 5, 4);

-- PROMOCIONES
INSERT INTO promocion (nombre, tipo_promocion, cantidad_minima, precio_final, activo) VALUES
-- Promociones DESCUENTO
('2x1 Base Líquida Infallible', 'DESCUENTO', 2, 8100.00, true),
('3x2 Máscara de Pestañas', 'DESCUENTO', 3, 6400.00, true),
('2x1 Serum Revitalift', 'DESCUENTO', 2, 10440.00, true),
-- Promociones COMBO  
('Kit Maquillaje Básico', 'COMBO', 1, 7500.00, true),
('Combo Cuidado Capilar', 'COMBO', 1, 5800.00, true),
('Pack Skincare Essential', 'COMBO', 1, 9500.00, true);

-- RELACIONES PROMOCION_PRODUCTO
INSERT INTO promocion_producto (promocion_id, producto_id) VALUES

                                                               (1, 1),
                                                               (2, 2),
                                                               (3, 5),
                                                               (4, 1), (4, 3), (4, 4),
                                                               (5, 8), (5, 9), (5, 10),
                                                               (6, 5), (6, 6), (6, 7);


-- PEDIDOS
INSERT INTO pedido
(fecha, total, cliente, estado_pedido, tipo_venta, localidad, codigo_postal, direccion, metodo_pago)
VALUES
    ('2024-01-21 10:30:00', 7300.00, 'María García', 'ENTREGADO', 'LOCAL', NULL, NULL, 'Tienda Central', 'EFECTIVO'),
    ('2024-01-21 11:45:00', 5800.00, 'Juan Pérez', 'EN_PREPARACION', 'ENVIO', 'Palermo', '1414', 'Av. Córdoba 1234', 'TRANSFERENCIA'),
    ('2024-01-21 14:20:00', 4500.00, 'Ana Martínez', 'PENDIENTE', 'LOCAL', NULL, NULL, 'Tienda Central', 'EFECTIVO'),
    ('2024-01-21 16:00:00', 28000.00, 'Carlos López', 'EN_PREPARACION', 'ENVIO', 'Belgrano', '1428', 'Cabildo 2345', 'TRANSFERENCIA'),
    ('2024-01-22 09:15:00', 3700.00, 'Laura Rodríguez', 'ENTREGADO', 'LOCAL', NULL, NULL, 'Tienda Central', 'EFECTIVO'),
    ('2024-01-22 11:00:00', 13500.00, 'Sofía Torres', 'ENTREGADO', 'LOCAL', NULL, NULL, 'Tienda Central', 'EFECTIVO'),
    ('2024-01-22 14:30:00', 15600.00, 'Martín Gómez', 'EN_PREPARACION', 'ENVIO', 'Recoleta', '1679', 'Uruguay 567', 'TRANSFERENCIA');

--DETALLE PEDIDO
INSERT INTO detalle_pedido
(cantidad, precio_unitario, sub_total, producto_id, pedido_id, promocion_id)
VALUES
    (1, 4500.00, 4500.00, 1, 1, NULL),
    (1, 2800.00, 2800.00, 3, 1, NULL),
    (1, 5800.00, 5800.00, 1, 2, NULL),
    (1, 4500.00, 4500.00, 1, 3, NULL),
    (1, 25000.00, 25000.00, 2, 4, NULL),
    (1, 3000.00, 3000.00, 4, 4, NULL),
    (1, 3700.00, 3700.00, 3, 5, NULL),
    (1, 7500.00, 7500.00, NULL, 6, 4),
    (1, 8100.00, 8100.00, NULL, 6, 1),
    (1, 5800.00, 5800.00, NULL, 7, 5),
    (1, 9500.00, 9500.00, NULL, 7, 6),
    (1, 3200.00, 3200.00, 2, 7, NULL);
