/*
 * SQLeo Visual Query Builder :: java database frontend with join definitions
 * Copyright (C) 2016 edinhojorge@users.sourceforge.net
 *  
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */
package com.sqleo.common.jdbc.wrapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;

import com.sqleo.common.jdbc.interceptor.SqlCommandInterceptor;

public class StatementWrapper extends AbstractWrapper implements Statement {

	private Statement originalStatement;
	
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return originalStatement.unwrap(iface);
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		return originalStatement.executeQuery(sql);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return originalStatement.isWrapperFor(iface);
	}

	public int executeUpdate(String sql) throws SQLException {
		isUpdatable(sql);
		return originalStatement.executeUpdate(sql);
	}

	public void close() throws SQLException {
		originalStatement.close();
	}

	public int getMaxFieldSize() throws SQLException {
		return originalStatement.getMaxFieldSize();
	}

	public void setMaxFieldSize(int max) throws SQLException {
		originalStatement.setMaxFieldSize(max);
	}

	public int getMaxRows() throws SQLException {
		return originalStatement.getMaxRows();
	}

	public void setMaxRows(int max) throws SQLException {
		originalStatement.setMaxRows(max);
	}

	public void setEscapeProcessing(boolean enable) throws SQLException {
		originalStatement.setEscapeProcessing(enable);
	}

	public int getQueryTimeout() throws SQLException {
		return originalStatement.getQueryTimeout();
	}

	public void setQueryTimeout(int seconds) throws SQLException {
		originalStatement.setQueryTimeout(seconds);
	}

	public void cancel() throws SQLException {
		originalStatement.cancel();
	}

	public SQLWarning getWarnings() throws SQLException {
		return originalStatement.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		originalStatement.clearWarnings();
	}

	public void setCursorName(String name) throws SQLException {
		originalStatement.setCursorName(name);
	}

	public boolean execute(String sql) throws SQLException {
		isUpdatable(sql);
		return originalStatement.execute(sql);
	}

	public ResultSet getResultSet() throws SQLException {
		return originalStatement.getResultSet();
	}

	public int getUpdateCount() throws SQLException {
		return originalStatement.getUpdateCount();
	}

	public boolean getMoreResults() throws SQLException {
		return originalStatement.getMoreResults();
	}

	public void setFetchDirection(int direction) throws SQLException {
		originalStatement.setFetchDirection(direction);
	}

	public int getFetchDirection() throws SQLException {
		return originalStatement.getFetchDirection();
	}

	public void setFetchSize(int rows) throws SQLException {
		originalStatement.setFetchSize(rows);
	}

	public int getFetchSize() throws SQLException {
		return originalStatement.getFetchSize();
	}

	public int getResultSetConcurrency() throws SQLException {
		return originalStatement.getResultSetConcurrency();
	}

	public int getResultSetType() throws SQLException {
		return originalStatement.getResultSetType();
	}

	public void addBatch(String sql) throws SQLException {
		originalStatement.addBatch(sql);
	}

	public void clearBatch() throws SQLException {
		originalStatement.clearBatch();
	}

	public int[] executeBatch() throws SQLException {
		return originalStatement.executeBatch();
	}

	public Connection getConnection() throws SQLException {
		return originalStatement.getConnection();
	}

	public boolean getMoreResults(int current) throws SQLException {
		return originalStatement.getMoreResults(current);
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		return originalStatement.getGeneratedKeys();
	}

	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		isUpdatable(sql);
		return originalStatement.executeUpdate(sql, autoGeneratedKeys);
	}

	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		isUpdatable(sql);
		return originalStatement.executeUpdate(sql, columnIndexes);
	}

	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		isUpdatable(sql);
		return originalStatement.executeUpdate(sql, columnNames);
	}

	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		isUpdatable(sql);
		return originalStatement.execute(sql, autoGeneratedKeys);
	}

	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		isUpdatable(sql);
		return originalStatement.execute(sql, columnIndexes);
	}

	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		isUpdatable(sql);
		return originalStatement.execute(sql, columnNames);
	}

	public int getResultSetHoldability() throws SQLException {
		return originalStatement.getResultSetHoldability();
	}

	public boolean isClosed() throws SQLException {
		return originalStatement.isClosed();
	}

	public void setPoolable(boolean poolable) throws SQLException {
		originalStatement.setPoolable(poolable);
	}

	public boolean isPoolable() throws SQLException {
		return originalStatement.isPoolable();
	}

	public void closeOnCompletion() throws SQLException {
		originalStatement.closeOnCompletion();
	}

	public boolean isCloseOnCompletion() throws SQLException {
		return originalStatement.isCloseOnCompletion();
	}

	public StatementWrapper(Statement statement, ArrayList<SqlCommandInterceptor> sqlCommandInterceptorList) {
		super(sqlCommandInterceptorList);
		this.originalStatement = statement;
	}

}
