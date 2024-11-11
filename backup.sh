#!/bin/bash
DATE=$(date +\%F)  # Get the current date (YYYY-MM-DD)
BACKUP_DIR="/home/dci-student/Documents/projects/thera-track"  # Set the directory where backups will be stored
DB_NAME="theratrack_db"  # Specify the name of the database you want to back up
USER="postgres"  # Specify the database user
HOST="localhost"  # Host of your PostgreSQL server (usually localhost)
PORT="5432"  # Default PostgreSQL port

# Perform the backup using pg_dump

pg_dump -h $HOST -p $PORT -U $USER -F c -b -v -f "$BACKUP_DIR/${DB_NAME}_$DATE.backup" $DB_NAME


