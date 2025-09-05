-- ==================== SQLite Schema (修复版本) ====================
-- For SQLite, save this as schema.sql

PRAGMA journal_mode=WAL;


-- Storage sessions table
CREATE TABLE IF NOT EXISTS storage_sessions (
    storage_id TEXT PRIMARY KEY,
    original_filename TEXT NOT NULL,
    file_size INTEGER NOT NULL,
    expected_checksum TEXT,
    total_chunks INTEGER NOT NULL,
    chunk_size INTEGER NOT NULL,
    -- 修复：使用 TEXT 类型存储 ISO 格式时间字符串
    created_time TEXT NOT NULL,
    last_access_time TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'COMPLETING', 'COMPLETED', 'CANCELLED', 'EXPIRED', 'FAILED')),
    temp_file_path TEXT,
    chunk_dir_path TEXT,
    metadata_file_path TEXT,
    -- 修复：使用 TEXT 而不是 JSON
    custom_metadata TEXT
);

CREATE INDEX IF NOT EXISTS idx_sessions_status ON storage_sessions(status);
CREATE INDEX IF NOT EXISTS idx_sessions_last_access ON storage_sessions(last_access_time);
CREATE INDEX IF NOT EXISTS idx_sessions_created_time ON storage_sessions(created_time);
CREATE INDEX IF NOT EXISTS idx_sessions_status_access ON storage_sessions(status, last_access_time);

-- Uploaded chunks table
CREATE TABLE IF NOT EXISTS uploaded_chunks (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    storage_id TEXT NOT NULL,
    chunk_index INTEGER NOT NULL,
    chunk_size INTEGER NOT NULL,
    chunk_checksum TEXT,
    -- 修复：使用 TEXT 类型存储 ISO 格式时间字符串
    uploaded_time TEXT NOT NULL,
    chunk_offset INTEGER NOT NULL,

    UNIQUE(storage_id, chunk_index),
    FOREIGN KEY (storage_id) REFERENCES storage_sessions(storage_id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_chunks_storage_id ON uploaded_chunks(storage_id);
CREATE INDEX IF NOT EXISTS idx_chunks_uploaded_time ON uploaded_chunks(uploaded_time);

-- ==================== 初始化测试数据 (SQLite 兼容) ====================

-- 清理可能存在的旧数据
DELETE FROM uploaded_chunks;
DELETE FROM storage_sessions;

-- ==================== SQLite 时间函数说明 ====================

/*
SQLite 时间函数说明：
1. datetime('now') - 返回当前 UTC 时间，格式：YYYY-MM-DD HH:MM:SS
2. datetime('now', '+1 hour') - 当前时间加1小时
3. datetime('now', '-25 hours') - 当前时间减25小时
4. date('now') - 只返回日期部分：YYYY-MM-DD
5. time('now') - 只返回时间部分：HH:MM:SS

时间比较：
- SQLite 可以直接比较 TEXT 格式的 ISO 时间字符串
- 示例：WHERE created_time < datetime('now', '-2 hours')

在应用代码中：
- Java 端使用 LocalDateTime
- 存储时转换为 ISO 字符串格式：yyyy-MM-dd HH:mm:ss.SSS
- 读取时从字符串解析为 LocalDateTime
*/

-- ==================== 清理脚本（可选） ====================

-- 如果需要重置数据，可以执行以下命令：
-- DROP TABLE IF EXISTS uploaded_chunks;
-- DROP TABLE IF EXISTS storage_sessions;