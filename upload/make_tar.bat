@echo off

echo "Making TAR for upload"
cd ./files
tar -czf ../deploy.tar.gz *
echo "Done!"
pause