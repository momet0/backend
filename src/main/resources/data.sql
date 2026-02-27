-- 1. USUARIOS
INSERT INTO users (username, username_busqueda, email, password) VALUES
    ('admin','admin','admin@stock.com', '$2a$12$WztRfWo6H63OfxatZepoPO0R9V5qU0r4i..pJolqkKiflLeGxukli'),
    ('María', 'maria', 'maria@stock.com', '$2a$12$7CNfRk4.O0cEHLFKYHfcJ.iKxI7LB3lSYc4tDHqpHLi77aERN2kmy');

-- 2. CATEGORIA
INSERT INTO categoria (nombre) VALUES
('Maquillaje'),
('Skincare'),
('Cuidado Capilar'),
('Fragancias'),
('Accesorios de Belleza');

-- 3. PROVEEDORES
INSERT INTO proveedores (nombre) VALUES
('L''Oréal Argentina S.A.'),
('Procter & Gamble Argentina'),
('Cosméticos Internacionales SRL'),
('Laboratorios Beiersdorf');

-- 4. MARCAS
INSERT INTO marcas (nombre) VALUES
('L''Oréal'),      -- ID 1
('Garnier'),      -- ID 2
('Elseve'),       -- ID 3
('Chanel'),       -- ID 4
('Dior'),         -- ID 5
('Giorgio Armani'),-- ID 6
('Nivea'),        -- ID 7
('Generic');      -- ID 8

-- 5. PRODUCTOS (Normalizado: nombre + linea + marca + tono + tamaño + categoria)
INSERT INTO producto
(nombre, precio, stock, min_stock, imagen_url, activo, nombre_busqueda, linea, marca_id, tono, tamaño, codigo_barra, categoria_id, fk_proveedor)
VALUES
('Base Líquida Infallible 24H', 4500.00, 25, 5, 'https://res.cloudinary.com/dyxhuuuf7/image/upload/v1771119272/ossono_crlpiv.webp' , true, 'base liquida infallible 24h infallible loreal natural beige 30ml maquillaje', 'Infallible', 1, 'Natural Beige', '30ml', '3600523374581', 1, 1),

('Máscara de Pestañas Volume Million Lashes', 3200.00, 40, 8, 'https://res.cloudinary.com/dyxhuuuf7/image/upload/v1771119272/ossono_crlpiv.webp' , true, 'mascara de pestañas volume million lashes volume million lashes loreal black 9ml maquillaje', 'Volume Million Lashes', 1, 'Black', '9ml', '3600523374598', 1, 1),

('Labial Color Riche Le Rouge', 2800.00, 35, 6, 'https://res.cloudinary.com/dyxhuuuf7/image/upload/v1771119272/ossono_crlpiv.webp', true, 'labial color riche le rouge color riche loreal rouge passion 3.5g maquillaje', 'Color Riche', 1, 'Rouge Passion', '3.5g', '3600523374604', 1, 1),

('Delineador Líquido Superliner', 2100.00, 30, 5, 'https://res.cloudinary.com/dyxhuuuf7/image/upload/v1771119272/ossono_crlpiv.webp', true, 'delineador liquido superliner superliner loreal black 1.2ml maquillaje', 'Superliner', 1, 'Black', '1.2ml', '3600523374611', 1, 1),

('Serum Revitalift Filler', 5800.00, 20, 4, 'https://res.cloudinary.com/dyxhuuuf7/image/upload/v1771119272/ossono_crlpiv.webp', true, 'serum revitalift filler revitalift filler loreal 30ml skincare', 'Revitalift Filler', 1, NULL, '30ml', '3600523374628', 2, 1),

('Crema Facial Revitalift Día', 4200.00, 25, 5, 'https://res.cloudinary.com/dyxhuuuf7/image/upload/v1771119272/ossono_crlpiv.webp', true, 'crema facial revitalift dia revitalift loreal 50ml skincare', 'Revitalift', 1, NULL, '50ml', '3600523374635', 2, 1),

('Tónico Hidratant Garnier', 1800.00, 45, 10, 'https://res.cloudinary.com/dyxhuuuf7/image/upload/v1771119272/ossono_crlpiv.webp', true, 'tonico hidratant garnier hidratant garnier 200ml skincare', 'Hidratant', 2, NULL, '200ml', '3600523374642', 2, 2),

('Shampoo Elseve Pure Zinc', 1500.00, 60, 12, 'https://res.cloudinary.com/dyxhuuuf7/image/upload/v1771119272/ossono_crlpiv.webp', true, 'shampoo elseve pure zinc pure zinc elseve 250ml cuidado capilar', 'Pure Zinc', 3, NULL, '250ml', '3600523374659', 3, 1),

('Acondicionador Elseve Color protector', 1600.00, 55, 10, 'https://res.cloudinary.com/dyxhuuuf7/image/upload/v1771119272/ossono_crlpiv.webp', true, 'acondicionador elseve color protector color protector elseve 200ml cuidado capilar', 'Color Protector', 3, NULL, '200ml', '3600523374666', 3, 1),

('Mascarilla Capilar Nutritive', 3500.00, 30, 6, 'https://res.cloudinary.com/dyxhuuuf7/image/upload/v1771119272/ossono_crlpiv.webp', true, 'mascarilla capilar nutritive nutritive elseve 300ml cuidado capilar', 'Nutritive', 3, NULL, '300ml', '3600523374673', 3, 1),

('Perfume Chanel N°5 Eau de Parfum', 25000.00, 8, 2, 'https://res.cloudinary.com/dyxhuuuf7/image/upload/v1771119272/ossono_crlpiv.webp', true, 'perfume chanel n5 eau de parfum n5 chanel 50ml fragancias', 'N°5', 4, NULL, '50ml', '3145891306215', 4, 3),

('Perfume Sauvage Dior', 22000.00, 10, 2, 'https://res.cloudinary.com/dyxhuuuf7/image/upload/v1771119272/ossono_crlpiv.webp', true, 'perfume sauvage dior sauvage dior 60ml fragancias', 'Sauvage', 5, NULL, '60ml', '3348901357204', 4, 3),

('Agua de Tocolia Giorgio Armani', 18500.00, 12, 3, 'https://res.cloudinary.com/dyxhuuuf7/image/upload/v1771119272/ossono_crlpiv.webp', true, 'agua de tocolia giorgio armani acqua di gio giorgio armani 100ml fragancias', 'Acqua di Giò', 6, NULL, '100ml', '3348901367808', 4, 3),

('Set de Pinceles Nivea', 3200.00, 25, 5, 'https://res.cloudinary.com/dyxhuuuf7/image/upload/v1771119272/ossono_crlpiv.webp', true, 'set de pinceles nivea beauty nivea 5 unidades accesorios de belleza', 'Beauty', 7, NULL, '5 unidades', '4005800344678', 5, 4),

('Esponja de Maquillaje Blend', 800.00, 50, 10, 'https://res.cloudinary.com/dyxhuuuf7/image/upload/v1771119272/ossono_crlpiv.webp', true, 'esponja de maquillaje blend blend generic universal accesorios de belleza', 'Blend', 8, NULL, 'Universal', '7798154712345', 5, 4);

-- 6. PROMOCIONES (Normalizado: nombre sin acentos)
INSERT INTO promocion (nombre, nombre_busqueda, tipo_promocion, cantidad_minima, precio_final, activo) VALUES
('2x1 Base Líquida Infallible', '2x1 base liquida infallible', 'DESCUENTO', 2, 8100.00, true),
('3x2 Máscara de Pestañas', '3x2 mascara de pestañas', 'DESCUENTO', 3, 6400.00, true),
('2x1 Serum Revitalift', '2x1 serum revitalift', 'DESCUENTO', 2, 10440.00, true),
('Kit Maquillaje Básico', 'kit maquillaje basico', 'COMBO', 1, 7500.00, true),
('Combo Cuidado Capilar', 'combo cuidado capilar', 'COMBO', 1, 5800.00, true),
('Pack Skincare Essential', 'pack skincare essential', 'COMBO', 1, 9500.00, true);

-- 7. RELACIONES PROMOCION_PRODUCTO
INSERT INTO promocion_producto (promocion_id, producto_id, cantidad) VALUES
(1, 1, 2), (2, 2, 3), (3, 5, 2), (4, 1, 1), (4, 3, 1), (4, 4, 1), (5, 8, 1), (5, 9, 1), (5, 10, 1), (6, 5, 1), (6, 6, 1), (6, 7, 1);

-- 8. MOVIMIENTO STOCK (estructura unificada)
INSERT INTO movimiento_stock
(tipo_cambio, valor_numerico, valor_anterior, valor_actual, fecha, producto_id, marca_id, proveedor_id, user_id, motivo)
VALUES
-- STOCK - Creaciones
('STOCK', 25, 0, 25, '2024-01-15 08:00:00', 1, NULL, NULL, 1, 'CREACION_PRODUCTO'),
('STOCK', 40, 0, 40, '2024-01-15 08:30:00', 2, NULL, NULL, 1, 'CREACION_PRODUCTO'),
('STOCK', 35, 0, 35, '2024-01-15 09:00:00', 3, NULL, NULL, 1, 'CREACION_PRODUCTO'),

-- STOCK - Reabastecimientos
('STOCK', 20, 5, 25, '2024-01-20 09:00:00', 1, NULL, NULL, 1, 'REABASTECIMIENTO'),
('STOCK', 30, 10, 40, '2024-01-20 10:00:00', 2, NULL, NULL, 1, 'REABASTECIMIENTO'),
('STOCK', 15, 20, 35, '2024-01-20 11:00:00', 3, NULL, NULL, 1, 'REABASTECIMIENTO'),
('STOCK', 25, 0, 25, '2024-01-20 12:00:00', 6, NULL, NULL, 1, 'REABASTECIMIENTO'),
('STOCK', 40, 15, 55, '2024-01-20 13:00:00', 9, NULL, NULL, 1, 'REABASTECIMIENTO'),

-- STOCK - Ventas (negativas)
('STOCK', -2, 25, 23, '2024-01-21 14:00:00', 1, NULL, NULL, 1, 'VENTA'),
('STOCK', -1, 40, 39, '2024-01-21 14:05:00', 2, NULL, NULL, 1, 'VENTA'),
('STOCK', -3, 20, 17, '2024-01-21 14:10:00', 5, NULL, NULL, 1, 'VENTA'),
('STOCK', -1, 8, 7, '2024-01-21 14:15:00', 13, NULL, NULL, 1, 'VENTA'),

-- STOCK - Ajustes
('STOCK', 5, 25, 30, '2024-01-22 16:00:00', 4, NULL, NULL, 1, 'AJUSTE_STOCK'),
('STOCK', 5, 30, 35, '2024-01-22 18:00:00', 4, NULL, NULL, 2, 'AJUSTE_STOCK'),

-- STOCK - Suspensión y activación
('STOCK', -35, 35, 0, '2024-01-23 10:00:00', 3, NULL, NULL, 1, 'SUSPENSION_PRODUCTO'),
('STOCK', 35, 0, 35, '2024-01-23 14:00:00', 3, NULL, NULL, 1, 'ACTIVACION_PRODUCTO'),

-- STOCK - Cancelación
('STOCK', 2, 23, 25, '2024-01-24 09:00:00', 1, NULL, NULL, 1, 'CANCELACION_PEDIDO'),

-- PRECIO - Ajuste manual individual (ejemplo)
('PRECIO', 20, 4500.00, 5400.00, '2024-01-25 10:00:00', 1, NULL, NULL, 1, 'AJUSTE_PRECIO_MANUAL'),

-- PRECIO - Aumento masivo por marca (ejemplo)
('PRECIO', 15, 2800.00, 3220.00, '2024-01-25 11:00:00', NULL, 1, NULL, 1, 'AUMENTO_MASIVO_PORCENTAJE');


-- 9. PEDIDOS (Normalizado: cliente sin acentos)
INSERT INTO pedido
(fecha, total, cliente, nombre_busqueda_cliente, estado_pedido, tipo_venta, localidad, codigo_postal, direccion, metodo_pago)
VALUES
('2024-01-21 10:30:00', 7300.00, 'María García', 'maria garcia', 'ENTREGADO', 'LOCAL', '-', '-', 'Tienda Central', 'EFECTIVO'),
('2024-01-21 11:45:00', 5800.00, 'Juan Pérez', 'juan perez', 'EN_PREPARACION', 'ENVIO', 'Palermo', '1414', 'Av. Córdoba 1234', 'TRANSFERENCIA'),
('2024-01-21 14:20:00', 4500.00, 'Ana Martínez', 'ana martinez', 'PENDIENTE', 'LOCAL', '-', '-', 'Tienda Central', 'EFECTIVO'),
('2024-01-21 16:00:00', 28000.00, 'Carlos López', 'carlos lopez', 'EN_PREPARACION', 'ENVIO', 'Belgrano', '1428', 'Cabildo 2345', 'TRANSFERENCIA'),
('2024-01-22 09:15:00', 3700.00, 'Laura Rodríguez', 'laura rodriguez', 'ENTREGADO', 'LOCAL', '-', '-', 'Tienda Central', 'EFECTIVO'),
('2024-01-22 11:00:00', 13500.00, 'Sofía Torres', 'sofia torres', 'ENTREGADO', 'LOCAL', '-', '-', 'Tienda Central', 'EFECTIVO'),
('2024-01-22 14:30:00', 15600.00, 'Martín Gómez', 'martin gomez', 'EN_PREPARACION', 'ENVIO', 'Recoleta', '1679', 'Uruguay 567', 'TRANSFERENCIA');

-- 10. DETALLE PEDIDO
INSERT INTO detalle_pedido (cantidad, precio_unitario, sub_total, producto_id, pedido_id, promocion_id) VALUES
(1, 4500.00, 4500.00, 1, 1, NULL), (1, 2800.00, 2800.00, 3, 1, NULL), (1, 5800.00, 5800.00, 1, 2, NULL),
(1, 4500.00, 4500.00, 1, 3, NULL), (1, 25000.00, 25000.00, 2, 4, NULL), (1, 3000.00, 3000.00, 4, 4, NULL),
(1, 3700.00, 3700.00, 3, 5, NULL), (1, 7500.00, 7500.00, NULL, 6, 4), (1, 8100.00, 8100.00, NULL, 6, 1),
(1, 5800.00, 5800.00, NULL, 7, 5), (1, 9500.00, 9500.00, NULL, 7, 6), (1, 3200.00, 3200.00, 2, 7, NULL);