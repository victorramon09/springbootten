#To install Docker and Docker Compose in your WSL environment, follow these steps:
# Update the package index and install prerequisites
sudo apt-get update
sudo apt-get install -y ca-certificates curl gnupg lsb-release

# Add Docker's official GPG key
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

# Set up the repository
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Install Docker Engine
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

# Start Docker service
sudo service docker start

# Configure Docker to start on boot
sudo systemctl enable docker.service
sudo systemctl enable containerd.service

# Add your user to the docker group so you can run Docker without sudo
sudo usermod -aG docker $USER

# Install Docker Compose
sudo apt-get install -y docker-compose

# Verify installations
docker --version
docker-compose --version

# After running these commands, you might need to log out and log back in for the group membership changes to take effect. Or, you can run:
newgrp docker
# To apply the changes immediately in your current session.

# Note: If you're using WSL2, you might also need to start the Docker service manually after each WSL restart:
sudo service docker start

