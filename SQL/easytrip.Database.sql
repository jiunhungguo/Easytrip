USE [master]
GO
/****** Object:  Database [easytrip]    Script Date: 16/04/2025 21.20.22 ******/
CREATE DATABASE [easytrip]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'easytrip', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\easytrip.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'easytrip_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\easytrip_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [easytrip] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [easytrip].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [easytrip] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [easytrip] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [easytrip] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [easytrip] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [easytrip] SET ARITHABORT OFF 
GO
ALTER DATABASE [easytrip] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [easytrip] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [easytrip] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [easytrip] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [easytrip] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [easytrip] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [easytrip] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [easytrip] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [easytrip] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [easytrip] SET  DISABLE_BROKER 
GO
ALTER DATABASE [easytrip] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [easytrip] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [easytrip] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [easytrip] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [easytrip] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [easytrip] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [easytrip] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [easytrip] SET RECOVERY FULL 
GO
ALTER DATABASE [easytrip] SET  MULTI_USER 
GO
ALTER DATABASE [easytrip] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [easytrip] SET DB_CHAINING OFF 
GO
ALTER DATABASE [easytrip] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [easytrip] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [easytrip] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [easytrip] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'easytrip', N'ON'
GO
ALTER DATABASE [easytrip] SET QUERY_STORE = ON
GO
ALTER DATABASE [easytrip] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
ALTER DATABASE [easytrip] SET  READ_WRITE 
GO
